package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.database.place.PlaceDbModel
import hr.dtakac.prognoza.data.database.place.toDbModel
import hr.dtakac.prognoza.data.database.place.toEntity
import hr.dtakac.prognoza.domain.repository.PlaceGetter
import hr.dtakac.prognoza.domain.repository.PlaceSaver
import hr.dtakac.prognoza.entities.Place

class PlaceRepository(
    private val placeDao: PlaceDao
) : PlaceGetter, PlaceSaver {
    override suspend fun get(latitude: Double, longitude: Double): Place? {
        return placeDao.get(latitude, longitude)?.toEntity()
    }

    override suspend fun getAll(): List<Place> {
        return placeDao.getPlaces().map(PlaceDbModel::toEntity)
    }

    override suspend fun save(place: Place) {
        placeDao.insert(place.toDbModel())
    }
}