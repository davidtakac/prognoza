package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place

interface PlaceRepository {
    suspend fun search(query: String): List<Place>
    suspend fun getSaved(): List<Place>
    suspend fun getSaved(latitude: Double, longitude: Double): Place?
    suspend fun save(place: Place)
}