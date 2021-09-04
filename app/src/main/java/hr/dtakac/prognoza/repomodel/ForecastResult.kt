package hr.dtakac.prognoza.repomodel

import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.dbmodel.ForecastMeta

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