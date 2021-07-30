package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.databasemodel.ForecastHours

interface ForecastRepository {
    suspend fun getTodayForecastHours(placeId: String): ForecastHours
    suspend fun getTomorrowForecastHours(placeId: String): ForecastHours
    suspend fun getOtherDaysForecastHours(placeId: String): ForecastHours
    suspend fun deleteExpiredData()
}