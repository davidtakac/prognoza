package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.PlaceRepository
import hr.dtakac.prognoza.shared.entity.Place

class GetSavedPlaces internal constructor(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<Place> = placeRepository.getAllSavedPlaces()
}