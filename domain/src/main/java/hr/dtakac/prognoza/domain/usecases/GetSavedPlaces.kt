package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.entities.Place

class GetSavedPlaces(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): List<Place> = placeRepository.getSaved()
}