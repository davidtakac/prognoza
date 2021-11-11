package hr.dtakac.prognoza.core.repository.place

import hr.dtakac.prognoza.core.model.database.Place

interface PlaceRepository {
    suspend fun get(placeId: String): Place?
    suspend fun getAll(): List<Place>
    suspend fun search(query: String): List<Place>
    suspend fun pick(place: Place)
    suspend fun isSaved(placeId: String): Boolean
    suspend fun isPicked(placeId: String): Boolean
    suspend fun deleteAll(placeIds: List<String>)
}