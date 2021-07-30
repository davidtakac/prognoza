package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.databasemodel.ForecastHours

interface ForecastRepository {
    suspend fun getTodayForecastHours(): ForecastHours
    suspend fun getTomorrowForecastHours(): ForecastHours
    suspend fun getOtherDaysForecastHours(): ForecastHours
}