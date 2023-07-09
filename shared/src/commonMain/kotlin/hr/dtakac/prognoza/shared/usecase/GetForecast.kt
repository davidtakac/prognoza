package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.data.ForecastRepository
import hr.dtakac.prognoza.shared.entity.Coordinates
import hr.dtakac.prognoza.shared.entity.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetForecast internal constructor(
  private val forecastRepository: ForecastRepository,
  private val getSelectedMeasurementSystem: GetSelectedMeasurementSystem,
  private val computationDispatcher: CoroutineDispatcher
) {
  suspend operator fun invoke(coordinates: Coordinates): Forecast? =
    forecastRepository.getForecast(coordinates)?.let {
      withContext(computationDispatcher) {
        it.toMeasurementSystem(getSelectedMeasurementSystem())
      }
    }
}