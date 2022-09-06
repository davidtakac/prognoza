package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.data.database.place.Place
import hr.dtakac.prognoza.repomodel.ForecastResult
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: hr.dtakac.prognoza.data.database.place.Place
    ): ForecastResult
    suspend fun deleteExpiredData()
}