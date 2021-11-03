package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.model.database.Place
import hr.dtakac.prognoza.model.repository.ForecastResult
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: Place
    ): ForecastResult
    suspend fun deleteExpiredData()
}