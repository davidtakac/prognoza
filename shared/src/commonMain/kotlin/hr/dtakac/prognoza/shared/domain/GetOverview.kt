package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.Forecast
import hr.dtakac.prognoza.shared.entity.Overview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetOverview internal constructor(
    private val getForecast: GetForecast,
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OverviewResult =
        when (val forecastResult = getForecast()) {
            ForecastResult.Failure -> OverviewResult.Failure
            ForecastResult.NoPlace -> OverviewResult.NoPlace
            is ForecastResult.Success -> OverviewResult.Success(buildOverview(forecastResult.forecast))
        }

    private suspend fun buildOverview(forecast: Forecast): Overview = withContext(computationDispatcher) {
        Overview(
            temperature = forecast.hours[0].temperature,
            minimumTemperature = forecast.days[0].minimumTemperature,
            maximumTemperature = forecast.da
        )
    }


}

sealed interface OverviewResult {
    object NoPlace : OverviewResult
    object Failure : OverviewResult
    data class Success(val overview: Overview) : OverviewResult
}