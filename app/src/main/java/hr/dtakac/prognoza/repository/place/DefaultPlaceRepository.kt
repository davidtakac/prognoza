package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.common.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.common.USER_AGENT
import hr.dtakac.prognoza.api.PlaceService
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.database.entity.Place
import kotlinx.coroutines.withContext

class DefaultPlaceRepository(
    private val placeDao: PlaceDao,
    private val placeService: PlaceService,
    private val dispatcherProvider: DispatcherProvider,
) : PlaceRepository {
    override suspend fun get(placeId: String): Place {
        val place = placeDao.get(placeId)
        return if (place == null) {
            val defaultPlace = Place(
                id = DEFAULT_PLACE_ID,
                fullName = "Osijek, Grad Osijek, Osijek-Baranja County, Croatia",
                latitude = 45.55f,
                longitude = 18.69f,
                isSaved = true
            )
            placeDao.insertOrUpdate(defaultPlace)
            defaultPlace
        } else {
            place
        }
    }

    override suspend fun getAll(): List<Place> {
        return placeDao.getAll()
    }

    override suspend fun search(query: String): List<Place> {
        val response = placeService.search(
            userAgent = USER_AGENT,
            query = query,
            format = "jsonv2"
        )
        return withContext(dispatcherProvider.default) {
            response.map {
                Place(
                    id = it.id,
                    fullName = it.displayName,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    isSaved = false
                )
            }
        }
    }

    override suspend fun save(place: Place) {
        placeDao.insertOrUpdate(if (!place.isSaved) place.copy(isSaved = true) else place)
    }
}