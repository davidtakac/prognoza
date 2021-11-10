package hr.dtakac.prognoza.core.model.repository

import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan

sealed interface ForecastResult

data class Success(
    val timeSpans: List<ForecastTimeSpan> = listOf()
) : ForecastResult

data class CachedSuccess(
    val success: Success = Success(),
    val reason: ForecastError? = null
) : ForecastResult

data class Empty(
    val reason: ForecastError? = null,
) : ForecastResult

object None : ForecastResult