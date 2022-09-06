package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.entities.Place

class SearchPlaces(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(query: String): List<Place> = placeRepository.search(query)
}