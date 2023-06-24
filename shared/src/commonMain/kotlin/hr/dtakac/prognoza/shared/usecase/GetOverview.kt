package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetOverview internal constructor(
    private val getForecast: GetForecast,
    private val getSelectedMeasurementSystem: GetSelectedMeasurementSystem,
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(coordinates: Coordinates): Overview? =
        getForecast(coordinates)?.let {
            withContext(computationDispatcher) {
                Overview.build(it, getSelectedMeasurementSystem())
            }
        }
}