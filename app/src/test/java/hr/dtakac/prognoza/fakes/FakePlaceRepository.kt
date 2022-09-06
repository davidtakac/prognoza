package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.data.database.place.Place
import hr.dtakac.prognoza.repository.place.PlaceRepository

class FakePlaceRepository : PlaceRepository {
    override suspend fun get(placeId: String): hr.dtakac.prognoza.data.database.place.Place? {
        return TEST_PLACE
    }

    override suspend fun getAll(): List<hr.dtakac.prognoza.data.database.place.Place> {
        return listOf(TEST_PLACE)
    }

    override suspend fun getDefaultPlace(): hr.dtakac.prognoza.data.database.place.Place {
        return TEST_PLACE
    }

    override suspend fun isSaved(placeId: String): Boolean {
        return placeId == TEST_PLACE.id
    }

    override suspend fun save(place: hr.dtakac.prognoza.data.database.place.Place) {
        // do nothing
    }

    override suspend fun search(query: String): List<hr.dtakac.prognoza.data.database.place.Place> {
        return getAll()
    }
}