package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastMeta

sealed class ForecastResult {
    data class Success(
        val meta: ForecastMeta?,
        val hours: List<ForecastHour>
    ): ForecastResult()

    data class CachedSuccess(
        val success: Success,
        val reasonResourceId: Int
    ): ForecastResult()

    object Empty : ForecastResult()

    data class EmptyWithReason(
        val reasonResourceId: Int,
    ): ForecastResult()
}