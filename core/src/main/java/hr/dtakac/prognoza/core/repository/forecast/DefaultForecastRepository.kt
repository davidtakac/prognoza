package hr.dtakac.prognoza.core.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.core.api.ForecastService
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.core.database.dao.ForecastInstantDao
import hr.dtakac.prognoza.core.model.api.ForecastTimeStep
import hr.dtakac.prognoza.core.model.api.LocationForecastResponse
import hr.dtakac.prognoza.core.model.database.*
import hr.dtakac.prognoza.core.model.repository.ForecastError
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.utils.USER_AGENT
import hr.dtakac.prognoza.core.utils.hasExpired
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.io.IOException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastInstantDao,
    private val metaRepository: MetaRepository,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository,
    private val dispatcherProvider: DispatcherProvider
) : ForecastRepository {

    private val _result = MutableStateFlow<ForecastResult>(ForecastResult.None)
    override val result: StateFlow<ForecastResult> get() = _result.asStateFlow()

    override suspend fun updateForecastResult(
        start: ZonedDateTime,
        end: ZonedDateTime
    ) {
        val selectedPlace = getSelectedPlace()
        if (selectedPlace == null) {
            _result.value = ForecastResult.Empty(ForecastError.NoSelectedPlace)
            return
        } else {
            val selectedPlaceMeta = getPlaceMeta(selectedPlace)
            var error: ForecastError? = null
            if (selectedPlaceMeta.hasExpired()) {
                try {
                    updateForecastDatabase(
                        place = selectedPlace,
                        lastModified = selectedPlaceMeta?.lastModified
                    )
                } catch (e: HttpException) {
                    error = getHttpForecastError(e)
                } catch (e: SQLiteException) {
                    error = ForecastError.Database(e)
                } catch (e: IOException) {
                    error = ForecastError.Io(e)
                } catch (e: Exception) {
                    error = ForecastError.Unknown(e)
                }
            }
            _result.value = try {
                val timeSpans = forecastDao.getForecastInstants(
                    start = start,
                    end = end,
                    placeId = selectedPlace.id
                )
                if (error == null) {
                    ForecastResult.Success(
                        timeSpans = timeSpans,
                        place = selectedPlace
                    )
                } else {
                    ForecastResult.Cached(
                        success = ForecastResult.Success(
                            timeSpans = timeSpans,
                            place = selectedPlace
                        ),
                        reason = error
                    )
                }
            } catch (e: SQLiteException) {
                ForecastResult.Empty(ForecastError.Database(e))
            } catch (e: Exception) {
                ForecastResult.Empty(ForecastError.Unknown(e))
            } finally {
                deleteExpiredData()
            }
        }
    }

    private fun getHttpForecastError(httpException: HttpException): ForecastError {
        return when (httpException.code()) {
            429 -> ForecastError.Throttling(httpException)
            in 400..499 -> ForecastError.Client(httpException)
            in 500..504 -> ForecastError.Server(httpException)
            else -> ForecastError.Unknown(httpException)
        }
    }

    private suspend fun updateForecastDatabase(place: Place, lastModified: ZonedDateTime?) {
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = lastModifiedTimestamp,
            latitude = format("%.2f", place.latitude),
            longitude = format("%.2f", place.longitude)
        )
        updateForecastTimeSpans(forecastResponse.body(), place.id)
        updateForecastMeta(forecastResponse.headers(), place.id)
    }

    private suspend fun updateForecastMeta(forecastResponseHeaders: Headers, placeId: String) {
        metaRepository.update(
            placeId,
            expiresTime = forecastResponseHeaders["Expires"],
            lastModifiedTime = forecastResponseHeaders["Last-Modified"],
        )
    }

    private suspend fun updateForecastTimeSpans(
        locationForecastResponse: LocationForecastResponse?,
        placeId: String
    ) {
        val forecastInstants =
            locationForecastResponse?.forecast?.forecastTimeSteps?.let { timeSteps ->
                withContext(dispatcherProvider.default) {
                    timeSteps.map {
                        it.toForecastInstant(placeId)
                    }
                }
            } ?: return
        forecastDao.insertOrUpdateAll(forecastInstants)
    }

    private suspend fun deleteExpiredData() {
        try {
            forecastDao.deletePastForecastInstants()
        } catch (e: Exception) {
            // intentionally ignored
            e.printStackTrace()
        }
    }

    private suspend fun getSelectedPlace(): Place? {
        return preferencesRepository.getSelectedPlaceId()?.let {
            placeRepository.get(it)
        }
    }

    private suspend fun getPlaceMeta(place: Place): ForecastMeta? {
        return metaRepository.get(place.id)
    }

    private fun ForecastTimeStep.toForecastInstant(placeId: String): ForecastInstant {
        return ForecastInstant(
            time = ZonedDateTime.parse(time),
            placeId = placeId,
            temperature = data.instant.data.airTemperature,
            windSpeed = data.instant.data.windSpeed,
            windFromDirection = data.instant.data.windFromDirection,
            relativeHumidity = data.instant.data.relativeHumidity,
            airPressure = data.instant.data.airPressureAtSeaLevel,

            nextOneHours = data.next1Hours?.let {
                NextOneHours(
                    // if next1Hours exists, it's precipitation amount does too.
                    precipitationAmount = it.details.precipitationAmount!!,
                    symbolCode = it.summary.symbolCode
                )
            },
            nextSixHours = data.next6Hours?.let {
                NextSixHours(
                    // if next6Hours exists, it's precipitation amount does too.
                    precipitationAmount = data.next6Hours.details.precipitationAmount!!,
                    symbolCode = data.next6Hours.summary.symbolCode
                )
            },
            nextTwelveHours = data.next12Hours?.let {
                NextTwelveHours(
                    symbolCode = data.next12Hours.summary.symbolCode
                )
            }
        )
    }
}