package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.entity.Place

interface PlaceRepository {
    suspend fun get(placeId: String): Place?
    suspend fun getDefaultPlace(): Place
    suspend fun getAll(): List<Place>
    suspend fun search(query: String): List<Place>
    suspend fun save(place: Place)
    suspend fun isSaved(placeId: String): Boolean
}