package hr.dtakac.prognoza.core.repository.forecast

import hr.dtakac.prognoza.core.model.repository.ForecastResult
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime

interface ForecastRepository {
    val result: StateFlow<ForecastResult>

    suspend fun updateForecastResult(
        start: ZonedDateTime,
        end: ZonedDateTime
    )
}