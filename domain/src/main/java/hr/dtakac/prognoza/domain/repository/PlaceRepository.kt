package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place

interface PlaceRepository {
    suspend fun search(query: String): PlaceRepositoryResult
    suspend fun getSaved(): PlaceRepositoryResult
    suspend fun getSaved(latitude: Double, longitude: Double): Place?
    suspend fun save(place: Place)
}

sealed interface PlaceRepositoryResult {
    object Error : PlaceRepositoryResult
    data class Success(
        val data: List<Place>
    ) : PlaceRepositoryResult
}