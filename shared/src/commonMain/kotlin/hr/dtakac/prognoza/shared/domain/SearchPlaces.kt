package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.PlaceSearcher
import hr.dtakac.prognoza.shared.domain.data.PlaceSearcherResult
import hr.dtakac.prognoza.shared.entity.Place
import hr.dtakac.prognoza.shared.platform.LocalRfc2616LanguageGetter

class SearchPlaces(
    private val placeSearcher: PlaceSearcher,
    private val localRfc2616LanguageGetter: LocalRfc2616LanguageGetter
) {
    suspend operator fun invoke(query: String): SearchPlacesResult {
        val result = placeSearcher.search(
            query,
            localRfc2616LanguageGetter.get()
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