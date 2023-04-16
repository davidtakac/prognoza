package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

interface PlaceRepository {
    suspend fun search(query: String): List<Place>?
    suspend fun get(id: String): Place?
    suspend fun getAll(): List<Place>
    suspend fun remove(id: String)
    suspend fun save(place: Place)
}