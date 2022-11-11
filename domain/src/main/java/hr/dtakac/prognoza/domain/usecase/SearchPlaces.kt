package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.place.Rfc2616LanguageGetter
import hr.dtakac.prognoza.domain.place.PlaceSearcher
import hr.dtakac.prognoza.domain.place.PlaceSearcherResult
import hr.dtakac.prognoza.entities.Place

class SearchPlaces(
    private val placeSearcher: PlaceSearcher,
    private val rfc2616LanguageGetter: Rfc2616LanguageGetter
) {
    suspend operator fun invoke(query: String): SearchPlacesResult {
        val result = placeSearcher.search(
            query,
            rfc2616LanguageGetter.get()
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