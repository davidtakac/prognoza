package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

internal interface PlaceSearcher {
    suspend fun search(
        query: String,
        rfc2616Language: String
    ): PlaceSearcherResult
}

sealed interface PlaceSearcherResult {
    object Error : PlaceSearcherResult
    data class Success(val places: List<Place>) : PlaceSearcherResult
}