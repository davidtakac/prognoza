package hr.dtakac.prognoza.domain.place

import hr.dtakac.prognoza.entities.Place

interface SavedPlaceGetter {
    suspend fun get(latitude: Double, longitude: Double): Place?
    suspend fun getAll(): List<Place>
}