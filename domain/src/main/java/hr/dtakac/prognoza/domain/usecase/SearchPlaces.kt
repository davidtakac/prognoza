package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepositoryResult
import hr.dtakac.prognoza.entities.Place

class SearchPlaces(private val placeRepository: PlaceRepository) {
    // todo: differentiate empty result and error
    suspend operator fun invoke(query: String): List<Place> {
        val result = placeRepository.search(query)
        return if (result is PlaceRepositoryResult.Success) result.data else listOf()
    }
}