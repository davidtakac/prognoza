package hr.dtakac.prognoza.repomodel

import hr.dtakac.prognoza.dbmodel.ForecastHour
import hr.dtakac.prognoza.dbmodel.ForecastMeta

sealed interface ForecastResult

data class Success(
    val meta: ForecastMeta?,
    val hours: List<ForecastHour>
) : ForecastResult

data class CachedSuccess(
    val success: Success,
    val reason: ForecastError?
) : ForecastResult

data class Empty(
    val reason: ForecastError?,
) : ForecastResult