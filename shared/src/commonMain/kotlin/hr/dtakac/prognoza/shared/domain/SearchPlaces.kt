package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.PlaceSearcher
import hr.dtakac.prognoza.shared.domain.data.PlaceSearcherResult
import hr.dtakac.prognoza.shared.entity.Place
import hr.dtakac.prognoza.shared.getLocalRfc2616LanguageCode

class SearchPlaces(
    private val placeSearcher: PlaceSearcher,
) {
    suspend operator fun invoke(query: String): SearchPlacesResult {
        val result = placeSearcher.search(
            query,
            getLocalRfc2616LanguageCode()
        )
        return if (result is PlaceSearcherResult.Success) {
            if (result.places.isEmpty()) {
                SearchPlacesResult.Empty.None
            } else {
                SearchPlacesResult.Success(result.places)
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