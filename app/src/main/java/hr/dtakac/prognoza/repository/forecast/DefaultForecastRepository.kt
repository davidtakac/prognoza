package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.MIN_DATE_TIME_RFC_1123
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.api.ForecastTimeStepData
import hr.dtakac.prognoza.api.LocationForecast
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.dao.ForecastHourDao
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastLocation
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.repository.location.LocationRepository
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DefaultForecastRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val forecastService: ForecastService,
    private val forecastDao: ForecastHourDao,
    private val locationRepository: LocationRepository,
    private val metaRepository: MetaRepository,
    private val preferencesRepository: PreferencesRepository
) : ForecastRepository {
    private val hoursAfterMidnightToShow = 6L

    override suspend fun getTodayForecastHours(): List<ForecastHour> {
        val anHourAgo = ZonedDateTime
            .now()
            .minusHours(1) // to get the current hour as well
        val hoursLeftInTheDay = 24 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastHours(
            start = anHourAgo,
            end = anHourAgo.plusHours(hoursToShow)
        )
    }

    override suspend fun getTomorrowForecastHours(): List<ForecastHour> {
        val tomorrow = ZonedDateTime
            .now()
            .atStartOfDay()
            .plusDays(1)
        return getForecastHours(
            start = tomorrow.plusHours(hoursAfterMidnightToShow + 1L /* start where today left off */),
            end = tomorrow.plusDays(1).plusHours(hoursAfterMidnightToShow)
        )
    }

    override suspend fun getAllForecastHours(
        start: ZonedDateTime
    ): List<ForecastHour> {
        return getForecastHours(
            start = start,
            end = start.plusWeeks(2L)
        )
    }

    private suspend fun getForecastHours(
        start: ZonedDateTime,
        end: ZonedDateTime
    ): List<ForecastHour> {
        val forecastMeta = metaRepository.get()
        if (hasCachedForecastExpired(forecastMeta)) {
            updateForecastDatabase(forecastMeta)
        }
        return forecastDao.getForecastHours(
            start = start,
            end = end,
            locationId = preferencesRepository.locationId
        )
    }

    private suspend fun updateForecastDatabase(forecastMeta: ForecastMeta?) {
        val forecastLocation = locationRepository.getSelectedLocation()
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = forecastMeta?.lastModified ?: MIN_DATE_TIME_RFC_1123,
            latitude = forecastLocation.latitude,
            longitude = forecastLocation.longitude
        )
        updateForecastMeta(forecastResponse.headers())
        updateForecastHours(forecastResponse.body(), forecastLocation)
    }

    private suspend fun updateForecastMeta(forecastResponseHeaders: Headers) {
        metaRepository.update(
            expiresTime = forecastResponseHeaders["Expires"],
            lastModifiedTime = forecastResponseHeaders["Last-Modified"]
        )
    }

    private suspend fun updateForecastHours(
        locationForecast: LocationForecast?,
        forecastLocation: ForecastLocation
    ) {
        val forecastHours = withContext(dispatcherProvider.default) {
            locationForecast?.forecast?.forecastTimeSteps?.map {
                ForecastHour(
                    time = ZonedDateTime.parse(it.time),
                    locationId = forecastLocation.id,
                    temperature = it.data.instant?.data?.airTemperature,
                    symbolCode = it.data.findSymbolCode(),
                    precipitationProbability = it.data.findProbabilityOfPrecipitation(),
                    precipitationAmount = it.data.findPrecipitationAmount(),
                    windSpeed = it.data.instant?.data?.windSpeed
                )
            }
        } ?: return
        forecastDao.insertOrUpdateAll(forecastHours)
    }

    private fun hasCachedForecastExpired(forecastMeta: ForecastMeta?) =
        forecastMeta == null || ZonedDateTime.parse(
            forecastMeta.expires,
            DateTimeFormatter.RFC_1123_DATE_TIME
        ) <= ZonedDateTime.now()

    private fun ForecastTimeStepData.findSymbolCode() =
        next1Hours?.summary?.symbolCode
            ?: next6Hours?.summary?.symbolCode
            ?: next12Hours?.summary?.symbolCode

    private fun ForecastTimeStepData.findProbabilityOfPrecipitation() =
        next1Hours?.data?.probabilityOfPrecipitation
            ?: next6Hours?.data?.probabilityOfPrecipitation
            ?: next12Hours?.data?.probabilityOfPrecipitation

    private fun ForecastTimeStepData.findPrecipitationAmount() =
        next1Hours?.data?.precipitationAmount
            ?: next6Hours?.data?.precipitationAmount
            ?: next12Hours?.data?.precipitationAmount
}