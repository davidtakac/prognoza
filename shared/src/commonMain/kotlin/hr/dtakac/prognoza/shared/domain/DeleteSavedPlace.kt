package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.ForecastRepository
import hr.dtakac.prognoza.shared.data.PlaceRepository

class DeleteSavedPlace internal constructor(
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(id: String) {
        placeRepository.remove(id)
        forecastRepository.delete(id)
    }
}