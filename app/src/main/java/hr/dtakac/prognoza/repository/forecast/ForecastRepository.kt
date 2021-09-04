package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.repomodel.ForecastResult

interface ForecastRepository {
    suspend fun getTodayForecastTimeSpans(placeId: String): ForecastResult
    suspend fun getTomorrowForecastTimeSpans(placeId: String): ForecastResult
    suspend fun getOtherDaysForecastTimeSpans(placeId: String): ForecastResult
    suspend fun deleteExpiredData()
}