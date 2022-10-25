package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepositoryResult
import hr.dtakac.prognoza.entities.Place

class GetSavedPlaces(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): GetSavedPlacesResult {
        val results = placeRepository.getSaved()
        return if (results is PlaceRepositoryResult.Success) {
            if (results.data.isEmpty()) {
                GetSavedPlacesResult.Empty.None
            } else {
                GetSavedPlacesResult.Success(results.data)
            }
        } else GetSavedPlacesResult.Empty.Error
    }
}

sealed interface GetSavedPlacesResult {
    data class Success(val places: List<Place>) : GetSavedPlacesResult
    sealed interface Empty : GetSavedPlacesResult {
        object None : Empty
        object Error : Empty
    }
}