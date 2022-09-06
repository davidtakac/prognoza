package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.data.database.place.Place

interface PlaceRepository {
    suspend fun get(placeId: String): hr.dtakac.prognoza.data.database.place.Place?
    suspend fun getDefaultPlace(): hr.dtakac.prognoza.data.database.place.Place
    suspend fun getAll(): List<hr.dtakac.prognoza.data.database.place.Place>
    suspend fun search(query: String): List<hr.dtakac.prognoza.data.database.place.Place>
    suspend fun save(place: hr.dtakac.prognoza.data.database.place.Place)
    suspend fun isSaved(placeId: String): Boolean
}