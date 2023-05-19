package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetOverview internal constructor(
    private val getForecast: GetForecast,
    private val getSelectedMeasurementSystem: GetSelectedMeasurementSystem,
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OverviewResult =
        when (val result = getForecast()) {
            ForecastResult.Failure -> OverviewResult.Failure
            ForecastResult.NoPlace -> OverviewResult.NoPlace
            is ForecastResult.Success -> OverviewResult.Success(
                withContext(computationDispatcher) {
                    Overview.create(result.forecast, getSelectedMeasurementSystem())
                }
            )
        }
}

sealed interface OverviewResult {
    object NoPlace : OverviewResult
    object Failure : OverviewResult
    data class Success(val overview: Overview) : OverviewResult
}