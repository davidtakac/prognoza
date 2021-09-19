package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.dbmodel.Place
import hr.dtakac.prognoza.repomodel.ForecastResult
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: Place
    ): ForecastResult
    suspend fun getTodayForecastTimeSpans(placeId: String): ForecastResult
    suspend fun getTomorrowForecastTimeSpans(placeId: String): ForecastResult
    suspend fun deleteExpiredData()
}