package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.repomodel.ForecastResult

interface ForecastRepository {
    suspend fun getTodayForecastHours(placeId: String): ForecastResult
    suspend fun getTomorrowForecastHours(placeId: String): ForecastResult
    suspend fun getOtherDaysForecastHours(placeId: String): ForecastResult
    suspend fun deleteExpiredData()
}