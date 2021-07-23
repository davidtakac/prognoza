package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.entity.ForecastHour
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getTodayForecastHours(): List<ForecastHour>
    suspend fun getTomorrowForecastHours(): List<ForecastHour>
    suspend fun getAllForecastHours(start: ZonedDateTime): List<ForecastHour>
}