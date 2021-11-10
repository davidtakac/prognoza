package hr.dtakac.prognoza.core.model.repository

import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place

sealed class ForecastResult {
    data class Success(
        val timeSpans: List<ForecastTimeSpan>,
        val place: Place
    ) : ForecastResult()

    data class Cached(
        val success: Success,
        val reason: ForecastError?
    ) : ForecastResult()

    data class Empty(
        val reason: ForecastError? = null,
    ) : ForecastResult()

    object None : ForecastResult()
}