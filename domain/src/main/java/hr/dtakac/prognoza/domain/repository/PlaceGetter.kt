package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place

interface PlaceGetter {
    suspend fun get(latitude: Double, longitude: Double): Place?
    suspend fun getAll(): List<Place>
}