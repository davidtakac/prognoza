package hr.dtakac.prognoza.repository.forecast

import android.database.sqlite.SQLiteException
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.apimodel.LocationForecastResponse
import hr.dtakac.prognoza.common.USER_AGENT
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.database.dao.ForecastHourDao
import hr.dtakac.prognoza.dbmodel.ForecastHour
import hr.dtakac.prognoza.repomodel.*
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.io.IOException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastHourDao,
    private val placeRepository: PlaceRepository,
    private val metaRepository: MetaRepository,
    private val dispatcherProvider: DispatcherProvider
) : ForecastRepository {
    private val hoursAfterMidnightToShow = 6L

    override suspend fun getTodayForecastHours(placeId: String): ForecastResult {
        val anHourAgo = ZonedDateTime
            .now()
            .minusHours(1) // to get the current hour as well
        val hoursLeftInTheDay = 24 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastHours(
            start = anHourAgo,
            end = anHourAgo.plusHours(hoursToShow),
            placeId
        )
    }

    override suspend fun getTomorrowForecastHours(placeId: String): ForecastResult {
        val tomorrow = ZonedDateTime
            .now()
            .atStartOfDay()
            .plusDays(1)
        return getForecastHours(
            start = tomorrow.plusHours(hoursAfterMidnightToShow + 1L /* start where today left off */),
            end = tomorrow.plusDays(1).plusHours(hoursAfterMidnightToShow),
            placeId
        )
    }

    override suspend fun getOtherDaysForecastHours(placeId: String): ForecastResult {
        val now = ZonedDateTime.now().atStartOfDay()
        return getForecastHours(
            start = now,
            end = now.plusDays(7L),
            placeId
        )
    }

    override suspend fun deleteExpiredData() {
        try {
            forecastDao.deleteExpiredForecastHours()
        } catch (e: Exception) {
            // intentionally ignored
            e.printStackTrace()
        }
    }

    private suspend fun getForecastHours(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): ForecastResult {
        var meta = try {
            metaRepository.get(placeId)
        } catch (e: Exception) {
            null
        }
        var error: ForecastError? = null
        if (meta?.hasExpired() != false) {
            try {
                updateForecastDatabase(placeId, meta?.lastModified)
                meta = metaRepository.get(placeId)
            } catch (e: HttpException) {
                error = handleHttpException(e)
            } catch (e: IOException) {
                error = IOError(e)
            } catch (e: SQLiteException) {
                error = DatabaseError(e)
            } catch (e: Exception) {
                error = UnknownError(e)
            }
        }
        return try {
            val hours = forecastDao.getForecastHours(start, end, placeId)
            hours.toForecastResult(meta, error)
        } catch (e: SQLiteException) {
            Empty(DatabaseError(e))
        } catch (e: Exception) {
            Empty(UnknownError(e))
        }
    }

    private fun handleHttpException(httpException: HttpException): ForecastError {
        return when (httpException.code()) {
            429 -> ThrottlingError(httpException)
            in 400..499 -> ClientError(httpException)
            in 500..504 -> ServerError(httpException)
            else -> UnknownError(httpException)
        }
    }

    private suspend fun updateForecastDatabase(placeId: String, lastModified: ZonedDateTime?) {
        val forecastPlace = placeRepository.get(placeId) ?: placeRepository.getDefaultPlace()
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = lastModifiedTimestamp,
            latitude = format("%.2f", forecastPlace.latitude),
            longitude = format("%.2f", forecastPlace.longitude)
        )
        updateForecastMeta(forecastResponse.headers(), forecastPlace.id)
        updateForecastHours(forecastResponse.body(), forecastPlace.id)
    }

    private suspend fun updateForecastMeta(forecastResponseHeaders: Headers, placeId: String) {
        metaRepository.update(
            placeId,
            expiresTime = forecastResponseHeaders["Expires"],
            lastModifiedTime = forecastResponseHeaders["Last-Modified"],
        )
    }

    private suspend fun updateForecastHours(
        locationForecastResponse: LocationForecastResponse?,
        placeId: String
    ) {
        val forecastHours = withContext(dispatcherProvider.default) {
            locationForecastResponse?.forecast?.forecastTimeSteps?.map {
                ForecastHour(
                    time = ZonedDateTime.parse(it.time),
                    placeId = placeId,
                    temperature = it.data?.instant?.data?.airTemperature,
                    symbolCode = it.data?.findSymbolCode(),
                    precipitationProbability = it.data?.findPrecipitationProbability(),
                    precipitationAmount = it.data?.findPrecipitationAmount(),
                    windSpeed = it.data?.instant?.data?.windSpeed,
                    windFromDirection = it.data?.instant?.data?.windFromDirection,
                    relativeHumidity = it.data?.instant?.data?.relativeHumidity,
                    pressure = it.data?.instant?.data?.airPressureAtSeaLevel
                )
            }
        } ?: return
        forecastDao.insertOrUpdateAll(forecastHours)
    }
}