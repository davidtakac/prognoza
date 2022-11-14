package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

interface SavedPlaceGetter {
    suspend fun get(latitude: Double, longitude: Double): Place?
    suspend fun getAll(): List<Place>
}