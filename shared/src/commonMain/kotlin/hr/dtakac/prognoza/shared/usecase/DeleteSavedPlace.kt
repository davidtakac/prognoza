package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.data.ForecastRepository
import hr.dtakac.prognoza.shared.data.PlaceRepository
import hr.dtakac.prognoza.shared.entity.Place

class DeleteSavedPlace internal constructor(
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(place: Place) {
        placeRepository.deleteSaved(place)
        forecastRepository.deleteForecast(place)
    }
}