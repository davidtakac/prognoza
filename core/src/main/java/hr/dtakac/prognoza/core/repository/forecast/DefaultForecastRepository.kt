package hr.dtakac.prognoza.core.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.core.api.ForecastService
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.core.database.dao.ForecastTimeSpanDao
import hr.dtakac.prognoza.core.model.api.LocationForecastResponse
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.*
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.utils.USER_AGENT
import hr.dtakac.prognoza.core.utils.hasExpired
import hr.dtakac.prognoza.core.utils.toForecastResult
import hr.dtakac.prognoza.core.utils.toForecastTimeSpan
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
    private val forecastDao: ForecastTimeSpanDao,
    private val metaRepository: MetaRepository,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository,
    private val dispatcherProvider: DispatcherProvider
) : ForecastRepository {

    private val _result = MutableStateFlow<ForecastResult>(None)
    override val result: StateFlow<ForecastResult> get() = _result.asStateFlow()

    override suspend fun updateForecastResult(
        start: ZonedDateTime,
        end: ZonedDateTime
    ) {
        val selectedPlace = getSelectedPlace()
        if (selectedPlace == null) {
            _result.value = Empty(NoSelectedPlaceForecastError)
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
                    error = DatabaseForecastError(e)
                } catch (e: IOException) {
                    error = IoForecastError(e)
                } catch (e: Exception) {
                    error = UnknownForecastError(e)
                }
            }
            _result.value = try {
                val timeSpans = forecastDao.getForecastTimeSpans(
                    start = start,
                    end = end,
                    placeId = selectedPlace.id
                )
                if (error == null) {
                    Success(timeSpans)
                } else {
                    CachedSuccess(
                        success = Success(timeSpans),
                        reason = error
                    )
                }
            } catch (e: SQLiteException) {
                Empty(DatabaseForecastError(e))
            } catch (e: Exception) {
                Empty(UnknownForecastError(e))
            } finally {
                deleteExpiredData()
            }
        }
    }

    private fun getHttpForecastError(httpException: HttpException): ForecastError {
        return when (httpException.code()) {
            429 -> ThrottlingForecastError(httpException)
            in 400..499 -> ClientForecastError(httpException)
            in 500..504 -> ServerForecastError(httpException)
            else -> UnknownForecastError(httpException)
        }
    }

    private suspend fun updateForecastDatabase(place: Place, lastModified: ZonedDateTime?) {
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompleteLocationForecast(
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
        val forecastTimeSpans = locationForecastResponse?.forecast?.forecastTimeSteps?.let {
            withContext(dispatcherProvider.default) {
                val result = mutableListOf<ForecastTimeSpan>()
                for (i in it.indices) {
                    val current = it[i]
                    val next = it.getOrNull(i + 1)
                    result.add(current.toForecastTimeSpan(placeId, next))
                }
                result
            }
        } ?: return
        forecastDao.insertOrUpdateAll(forecastTimeSpans)
    }

    private suspend fun deleteExpiredData() {
        try {
            forecastDao.deleteExpiredForecastTimeSpans()
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
}