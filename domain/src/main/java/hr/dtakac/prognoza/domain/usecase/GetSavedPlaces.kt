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
                GetSavedPlacesResult.None
            } else {
                GetSavedPlacesResult.Success(results.data)
            }
        } else GetSavedPlacesResult.Error
    }
}

sealed interface GetSavedPlacesResult {
    object None : GetSavedPlacesResult
    object Error : GetSavedPlacesResult
    data class Success(val places: List<Place>) : GetSavedPlacesResult
}