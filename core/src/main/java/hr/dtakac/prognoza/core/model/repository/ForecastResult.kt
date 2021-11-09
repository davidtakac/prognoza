package hr.dtakac.prognoza.core.model.repository

import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan

sealed interface ForecastResult

data class Success(
    val timeSpans: List<ForecastTimeSpan>
) : ForecastResult

data class CachedSuccess(
    val success: Success,
    val reason: ForecastError?
) : ForecastResult

data class Empty(
    val reason: ForecastError?,
) : ForecastResult