package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Coordinates
import hr.dtakac.prognoza.shared.entity.Place

class PlaceRepository {
    suspend fun search(query: String): List<Place>? {
        return null
    }

    suspend fun save(place: Place) { /*todo*/ }

    suspend fun getSaved(coordinates: Coordinates): Place? = null

    suspend fun getAllSaved(): List<Place> {
        return listOf()
    }

    suspend fun deleteSaved(place: Place) { /*todo*/ }
}