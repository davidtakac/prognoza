package hr.dtakac.prognoza.domain.place

import hr.dtakac.prognoza.entities.Place

interface PlaceSearcher {
    suspend fun search(
        query: String,
        rfc2616Language: String
    ): PlaceSearcherResult
}

sealed interface PlaceSearcherResult {
    object Error : PlaceSearcherResult
    data class Success(val places: List<Place>) : PlaceSearcherResult
}