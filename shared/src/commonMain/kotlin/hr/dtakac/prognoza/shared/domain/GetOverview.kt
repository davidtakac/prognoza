package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.Overview

class GetOverview internal constructor(private val getForecast: GetForecast) {
    suspend operator fun invoke(): OverviewResult =
        when (val forecastResult = getForecast()) {
            ForecastResult.Failure -> OverviewResult.Failure
            ForecastResult.NoPlace -> OverviewResult.NoPlace
            is ForecastResult.Success -> OverviewResult.Success(Overview(forecastResult.forecast))
        }
}

sealed interface OverviewResult {
    object NoPlace : OverviewResult
    object Failure : OverviewResult
    data class Success(val overview: Overview) : OverviewResult
}