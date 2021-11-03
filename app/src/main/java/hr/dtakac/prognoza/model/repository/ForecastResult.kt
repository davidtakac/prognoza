package hr.dtakac.prognoza.model.repository

import hr.dtakac.prognoza.model.database.ForecastMeta
import hr.dtakac.prognoza.model.database.ForecastTimeSpan

sealed interface ForecastResult

data class Success(
    val meta: ForecastMeta?,
    val timeSpans: List<ForecastTimeSpan>
) : ForecastResult

data class CachedSuccess(
    val success: Success,
    val reason: ForecastError?
) : ForecastResult

data class Empty(
    val reason: ForecastError?,
) : ForecastResult