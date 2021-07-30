package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.database.entity.Place

interface PlaceRepository {
    suspend fun getSelectedPlace(): Place
    suspend fun getSavedPlaces(): List<Place>
    suspend fun search(query: String): List<Place>
    suspend fun selectPlace(place: Place)
}