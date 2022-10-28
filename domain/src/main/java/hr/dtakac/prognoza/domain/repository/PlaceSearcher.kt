package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place

interface PlaceSearcher {
    suspend fun search(query: String): PlaceSearcherResult
}

sealed interface PlaceSearcherResult {
    object Error : PlaceSearcherResult
    data class Success(val places: List<Place>) : PlaceSearcherResult
}