package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

interface PlaceRepository {
    suspend fun get(latitude: Double, longitude: Double): Place?
    suspend fun getAll(): List<Place>
    suspend fun remove(latitude: Double, longitude: Double)
    suspend fun save(place: Place)
}