package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepositoryResult
import hr.dtakac.prognoza.entities.Place

class SearchPlaces(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(query: String): SearchPlacesResult {
        val result = placeRepository.search(query)
        return if (result is PlaceRepositoryResult.Success) {
            if (result.data.isEmpty()) {
                SearchPlacesResult.Empty.None
            } else {
                SearchPlacesResult.Success(result.data)
            }
        } else SearchPlacesResult.Empty.Error
    }
}

sealed interface SearchPlacesResult {
    data class Success(val places: List<Place>) : SearchPlacesResult
    sealed interface Empty : SearchPlacesResult {
        object None : Empty
        object Error : Empty
    }
}