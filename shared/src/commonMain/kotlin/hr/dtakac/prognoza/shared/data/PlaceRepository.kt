package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Place
import kotlinx.datetime.TimeZone

class PlaceRepository {
    suspend fun search(query: String): List<Place>? {
        return null
    }

    suspend fun savePlace(place: Place) { /*todo*/ }

    suspend fun getSavedPlace(id: String): Place? = if (id == "1") Place(
        id = "1",
        name = "Osijek",
        timeZone = TimeZone.of("Europe/Zagreb"),
        details = "Osjecko-baranjska",
        latitude = 45.55111,
        longitude = 18.69389
    ) else null

    suspend fun getAllSavedPlaces(): List<Place> {
        return listOf()
    }

    suspend fun deleteSavedPlace(id: String) { /*todo*/ }
}