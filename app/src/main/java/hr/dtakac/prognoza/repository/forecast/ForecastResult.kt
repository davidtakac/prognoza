package hr.dtakac.prognoza.repository.forecast

import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastMeta

sealed class ForecastResult {
    data class Success(
        val meta: ForecastMeta?,
        val hours: List<ForecastHour>
    ): ForecastResult()

    data class Error(
        val errorMessageResourceId: Int
    ): ForecastResult()
}