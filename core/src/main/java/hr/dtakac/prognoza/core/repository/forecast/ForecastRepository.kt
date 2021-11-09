package hr.dtakac.prognoza.core.repository.forecast

import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        place: Place,
        oldMeta: ForecastMeta?
    ): ForecastResult

    suspend fun deleteExpiredData()
}