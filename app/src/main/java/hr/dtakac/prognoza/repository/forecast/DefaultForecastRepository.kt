package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.GMT_ZONE_ID
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.api.ForecastTimeStepData
import hr.dtakac.prognoza.api.LocationForecast
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.AppDatabase
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastLocation
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.withContext
import okhttp3.Headers
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DefaultForecastRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val forecastService: ForecastService,
    private val appDatabase: AppDatabase,
    private val preferencesRepository: PreferencesRepository
) : ForecastRepository {
    private val minimumDateTimeRfc1123 = "Thu, 1 January 1970 00:00:00 GMT"
    private val hoursAfterMidnightToShow = 6L

    override suspend fun getTodayForecastHours(): List<ForecastHour> {
        val anHourAgo = ZonedDateTime
            .now()
            .minusHours(1) // to get the current hour as well
            .withZoneSameInstant(GMT_ZONE_ID)
            .toLocalDateTime()
        val hoursLeftInTheDay = 23 - 1 - anHourAgo.hour
        val hoursToShow = hoursLeftInTheDay + hoursAfterMidnightToShow
        return getForecastHours(
            startDateTimeGmt = anHourAgo,
            endDateTimeGmt = anHourAgo.plusHours(hoursToShow)
        )
    }

    override suspend fun getTomorrowForecastHours(): List<ForecastHour> {
        val tomorrow = ZonedDateTime
            .now()
            .atStartOfDay()
            .plusDays(1)
        return getForecastHours(
            startDateTimeGmt = tomorrow
                .plusHours(hoursAfterMidnightToShow + 1L)
                .withZoneSameInstant(GMT_ZONE_ID)
                .toLocalDateTime(),
            endDateTimeGmt = tomorrow
                .plusDays(1)
                .plusHours(hoursAfterMidnightToShow)
                .withZoneSameInstant(GMT_ZONE_ID)
                .toLocalDateTime()
        )
    }

    override suspend fun getAllForecastHours(
        startDateTimeGmt: ZonedDateTime
    ): List<ForecastHour> {
        return getForecastHours(
            startDateTimeGmt = startDateTimeGmt.toLocalDateTime(),
            endDateTimeGmt = startDateTimeGmt.plusWeeks(2L).toLocalDateTime()
        )
    }

    private suspend fun getForecastHours(
        startDateTimeGmt: LocalDateTime,
        endDateTimeGmt: LocalDateTime
    ): List<ForecastHour> {
        val forecastMeta = appDatabase.metaDao().get()
        if (hasCachedForecastExpired(forecastMeta)) {
            updateForecastDatabase(forecastMeta)
        }
        return appDatabase.hourDao().getForecastHours(
            startDateTimeGmt = startDateTimeGmt.format(DateTimeFormatter.ISO_DATE_TIME),
            endDateTimeGmt = endDateTimeGmt.format(DateTimeFormatter.ISO_DATE_TIME),
            locationId = preferencesRepository.locationId
        )
    }

    private suspend fun updateForecastDatabase(forecastMeta: ForecastMeta?) {
        val forecastLocation = appDatabase.locationDao().get(preferencesRepository.locationId)
            // todo: throw exception if not initialized
            ?: ForecastLocation(1, "Osijek", "31000", "Croatia", 45f, 18f)
        val forecastResponse = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = forecastMeta?.lastModified ?: minimumDateTimeRfc1123,
            latitude = forecastLocation.latitude,
            longitude = forecastLocation.longitude
        )
        updateForecastMeta(forecastResponse.headers())
        updateForecastHours(forecastResponse.body(), forecastLocation)
    }

    private suspend fun updateForecastMeta(forecastResponseHeaders: Headers) {
        val newForecastMeta = ForecastMeta(
            expires = forecastResponseHeaders["Expires"] ?: minimumDateTimeRfc1123,
            lastModified = forecastResponseHeaders["Last-Modified"] ?: minimumDateTimeRfc1123
        )
        appDatabase.metaDao().updateForecastMeta(newForecastMeta)
    }

    private suspend fun updateForecastHours(
        locationForecast: LocationForecast?,
        forecastLocation: ForecastLocation
    ) {
        val forecastHours = withContext(dispatcherProvider.default) {
            locationForecast?.forecast?.forecastTimeSteps?.map {
                ForecastHour(
                    timestamp = it.time,
                    locationId = forecastLocation.id,
                    temperature = it.data.instant?.data?.airTemperature,
                    symbolCode = it.data.findSymbolCode(),
                    precipitationProbability = it.data.findProbabilityOfPrecipitation(),
                    precipitationAmount = it.data.findPrecipitationAmount(),
                    windSpeed = it.data.instant?.data?.windSpeed
                )
            }
        } ?: return
        appDatabase.hourDao().insertOrUpdateAll(forecastHours)
    }

    private fun hasCachedForecastExpired(forecastMeta: ForecastMeta?) =
        forecastMeta == null || ZonedDateTime.parse(
            forecastMeta.expires,
            DateTimeFormatter.RFC_1123_DATE_TIME
        ) <= ZonedDateTime.now()

    private fun ForecastTimeStepData.findSymbolCode(): String? {
        return next1Hours?.summary?.symbolCode
            ?: next6Hours?.summary?.symbolCode
            ?: next12Hours?.summary?.symbolCode
    }

    private fun ForecastTimeStepData.findProbabilityOfPrecipitation(): Float? {
        return next1Hours?.data?.probabilityOfPrecipitation
            ?: next6Hours?.data?.probabilityOfPrecipitation
            ?: next12Hours?.data?.probabilityOfPrecipitation
    }

    private fun ForecastTimeStepData.findPrecipitationAmount(): Float? {
        return next1Hours?.data?.precipitationAmount
            ?: next6Hours?.data?.precipitationAmount
            ?: next12Hours?.data?.precipitationAmount
    }
}