package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.database.entity.Place

interface PlaceRepository {
    suspend fun get(placeId: String): Place
    suspend fun getAll(): List<Place>
    suspend fun search(query: String): List<Place>
    suspend fun save(place: Place)
}