package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.GMT_ZONE_ID
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.api.LocationForecast
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.AppDatabase
import hr.dtakac.prognoza.database.entity.ForecastDay
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastLocation
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.getTomorrow
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

    override suspend fun getRestOfDayForecastHours(): List<ForecastHour> {
        val anHourAgo = ZonedDateTime
            .now()
            .minusHours(1)
            .withZoneSameInstant(GMT_ZONE_ID)
            .toLocalDateTime()
        val minimumHoursToShow = 6
        val hoursLeftInTheDay = 22 - anHourAgo.hour
        val hoursToShow = if (hoursLeftInTheDay < minimumHoursToShow) {
            minimumHoursToShow
        } else {
            hoursLeftInTheDay
        }
        return getForecastHours(
            startDateTimeGmt = anHourAgo,
            endDateTimeGmt = anHourAgo.plusHours(hoursToShow.toLong())
        )
    }

    override suspend fun getTomorrowForecastHours(): List<ForecastHour> {
        val tomorrow = getTomorrow()
            .withZoneSameInstant(GMT_ZONE_ID)
            .toLocalDateTime()
        return getForecastHours(
            startDateTimeGmt = tomorrow,
            endDateTimeGmt = tomorrow.plusDays(1)
        )
    }

    override suspend fun getForecastDays(): List<ForecastDay> {
        TODO("Not yet implemented")
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
                    symbolCode = it.data.next1Hours?.summary?.symbolCode,
                    precipitationProbability = it.data.next1Hours?.data?.probabilityOfPrecipitation,
                    precipitationAmount = it.data.next1Hours?.data?.precipitationAmount,
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
}