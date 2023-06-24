package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.data.ForecastRepository
import hr.dtakac.prognoza.shared.entity.Coordinates
import hr.dtakac.prognoza.shared.entity.Forecast

class GetForecast internal constructor(private val forecastRepository: ForecastRepository) {
    suspend operator fun invoke(coordinates: Coordinates): Forecast? = forecastRepository.getForecast(coordinates)
}