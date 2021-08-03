package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.MIN_DATE_TIME_RFC_1123
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.*
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.common.network.NetworkChecker
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.database.dao.ForecastHourDao
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.hasExpired
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.internal.format
import retrofit2.HttpException
import java.time.ZonedDateTime

class DefaultForecastRepository(
    private val forecastService: ForecastService,
    private val forecastDao: ForecastHourDao,
    private val placeRepository: PlaceRepository,
    private val metaRepository: MetaRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkChecker: NetworkChecker
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
        val startDaysOffset = 2L
        return getForecastHours(
            start = now.plusDays(startDaysOffset),
            end = now.plusDays(startDaysOffset + 5L),
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
        return try {
            val currentMeta = metaRepository.get(placeId)
            var wasMetaUpdated = false
            if (currentMeta?.hasExpired() != false && networkChecker.hasInternetConnection()) {
                updateForecastDatabase(placeId, currentMeta?.lastModified)
                wasMetaUpdated = true
            }
            val hours = forecastDao.getForecastHours(start, end, placeId)
            if (hours.isNullOrEmpty()) {
                ForecastResult.Error(R.string.error_forecast_empty)
            } else {
                ForecastResult.Success(
                    if (wasMetaUpdated) metaRepository.get(placeId) else currentMeta,
                    hours
                )
            }
        } catch (httpException: HttpException) {
            ForecastResult.Error(
                when (httpException.code()) {
                    in 400..499 -> R.string.error_met_client
                    in 500..504 -> R.string.error_met_server
                    else -> R.string.error_met_unknown
                }
            )
        } catch (e: Exception) {
            ForecastResult.Error(R.string.error_generic)
        }
    }

    private suspend fun updateForecastDatabase(placeId: String, lastModified: ZonedDateTime?) {
        val forecastPlace = placeRepository.get(placeId)
        val lastModifiedTimestamp = ForecastMetaDateTimeConverter.toTimestamp(lastModified)
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = lastModifiedTimestamp ?: MIN_DATE_TIME_RFC_1123,
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
                    windFromDirection = it.data?.instant?.data?.windFromDirection
                )
            }
        } ?: return
        forecastDao.insertOrUpdateAll(forecastHours)
    }
}