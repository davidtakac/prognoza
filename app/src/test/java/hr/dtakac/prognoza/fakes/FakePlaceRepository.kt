package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.repository.place.PlaceRepository

class FakePlaceRepository : PlaceRepository {
    override suspend fun get(placeId: String): Place? {
        return TEST_PLACE
    }

    override suspend fun getAll(): List<Place> {
        return listOf(TEST_PLACE)
    }

    override suspend fun getDefaultPlace(): Place {
        return TEST_PLACE
    }

    override suspend fun isSaved(placeId: String): Boolean {
        return placeId == TEST_PLACE.id
    }

    override suspend fun save(place: Place) {
        // do nothing
    }

    override suspend fun search(query: String): List<Place> {
        return getAll()
    }
}