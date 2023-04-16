package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.ForecastRepository
import hr.dtakac.prognoza.shared.domain.data.PlaceRepository
import hr.dtakac.prognoza.shared.entity.Place

class DeleteSavedPlace internal constructor(
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(place: Place) {
        placeRepository.remove(latitude = place.latitude, longitude = place.longitude)
        forecastRepository.delete(latitude = place.latitude, longitude = place.longitude)
    }
}