package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.entity.ForecastHour

interface ForecastRepository {
    suspend fun getTodayForecastHours(): List<ForecastHour>
    suspend fun getTomorrowForecastHours(): List<ForecastHour>
    suspend fun getOtherDaysForecastHours(): List<ForecastHour>
}