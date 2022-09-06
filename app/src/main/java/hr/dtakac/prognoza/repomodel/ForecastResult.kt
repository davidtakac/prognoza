package hr.dtakac.prognoza.repomodel

import hr.dtakac.prognoza.data.database.forecast.ForecastMeta
import hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan

sealed interface ForecastResult

data class Success(
    val meta: hr.dtakac.prognoza.data.database.forecast.ForecastMeta?,
    val timeSpans: List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan>
) : ForecastResult

data class CachedSuccess(
    val success: Success,
    val reason: ForecastError?
) : ForecastResult

data class Empty(
    val reason: ForecastError?,
) : ForecastResult