package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Place
import kotlinx.datetime.TimeZone

class PlaceRepository {
    suspend fun search(query: String): List<Place>? {
        return null
    }

    // todo: pull from local storage
    suspend fun get(id: String): Place? = Place(
        id = "1",
        name = "Osijek",
        timeZone = TimeZone.of("Europe/Zagreb"),
        details = "Osjecko-baranjska",
        latitude = 45.55111,
        longitude = 18.69389
    )

    suspend fun getAll(): List<Place> {
        return listOf()
    }

    suspend fun remove(id: String) { /*todo*/ }

    suspend fun save(place: Place) { /*todo*/ }
}