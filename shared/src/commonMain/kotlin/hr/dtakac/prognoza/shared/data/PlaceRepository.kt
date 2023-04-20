package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Place

class PlaceRepository {
    suspend fun search(query: String): List<Place>? {
        return null
    }

    suspend fun get(id: String): Place? {
        return null
    }
    suspend fun getAll(): List<Place> {
        return listOf()
    }

    suspend fun remove(id: String) { /*todo*/ }

    suspend fun save(place: Place) { /*todo*/ }
}