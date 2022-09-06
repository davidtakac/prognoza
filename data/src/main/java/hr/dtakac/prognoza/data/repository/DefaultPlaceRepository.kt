package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.mapping.mapDbModelToEntity
import hr.dtakac.prognoza.data.mapping.mapEntityToDbModel
import hr.dtakac.prognoza.data.mapping.mapResponseToEntity
import hr.dtakac.prognoza.data.network.place.PlaceService
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.entities.Place
import java.util.*

class DefaultPlaceRepository(
    private val placeService: PlaceService,
    private val placeDao: PlaceDao,
    private val userAgent: String
) : PlaceRepository {
    override suspend fun getSaved(latitude: Double, longitude: Double): Place? {
        return placeDao.get(latitude, longitude)?.let { mapDbModelToEntity(it) }
    }

    override suspend fun getSaved(): List<Place> {
        return placeDao.getAll().map { mapDbModelToEntity(it) }
    }

    override suspend fun search(query: String): List<Place> {
        return placeService.search(
            userAgent = userAgent,
            acceptLanguage = Locale.getDefault().language,
            format = "jsonv2",
            query = query
        ).map(::mapResponseToEntity)
    }

    override suspend fun save(place: Place) {
        placeDao.insertOrUpdate(mapEntityToDbModel(place))
    }
}