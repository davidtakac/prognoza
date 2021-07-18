package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.ForecastDatabase
import hr.dtakac.prognoza.database.entity.ForecastDay
import hr.dtakac.prognoza.database.entity.ForecastHour
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DefaultForecastRepository(
    private val dispatcherProvider: DispatcherProvider,
    private val forecastService: ForecastService,
    private val forecastDatabase: ForecastDatabase
) : ForecastRepository {
    override suspend fun getForecastToday(): List<ForecastHour> {
        val meta = forecastDatabase.metaDao().get()
        if (meta != null && LocalDateTime.parse(meta.expires, DateTimeFormatter.RFC_1123_DATE_TIME) > LocalDateTime.now()) {
            TODO("Return directly from database without making a request.")
        }

        val response = forecastService.getCompactLocationForecast(
            userAgent = USER_AGENT,
            ifModifiedSince = meta?.lastModified ?: LocalDateTime.MIN.format(DateTimeFormatter.RFC_1123_DATE_TIME),
            latitude = 45f,
            longitude = 18f
        )
        TODO("Map to database object, persist to db in async and then return")
    }

    override suspend fun getForecastTomorrow(): List<ForecastHour> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastDays(): List<ForecastDay> {
        TODO("Not yet implemented")
    }
}