package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.data.PlaceRepository
import hr.dtakac.prognoza.shared.entity.Place

class SearchPlaces internal constructor(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(query: String): List<Place>? = placeRepository.search(query)
}