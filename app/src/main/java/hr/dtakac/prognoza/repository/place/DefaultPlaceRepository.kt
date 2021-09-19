package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.USER_AGENT
import hr.dtakac.prognoza.api.PlaceService
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.entity.Place
import kotlinx.coroutines.withContext
import java.util.*

class DefaultPlaceRepository(
    private val placeDao: PlaceDao,
    private val placeService: PlaceService,
    private val dispatcherProvider: DispatcherProvider,
) : PlaceRepository {

    override suspend fun get(placeId: String): Place? {
        return placeDao.get(placeId)
    }

    override suspend fun getDefaultPlace(): Place {
        var defaultPlace = get(DEFAULT_PLACE_ID)
        return if (defaultPlace == null) {
            defaultPlace = Place(
                id = DEFAULT_PLACE_ID,
                fullName = "Osijek, Grad Osijek, Osijek-Baranja County, Croatia",
                latitude = 45.55,
                longitude = 18.69
            )
            placeDao.insertOrUpdate(defaultPlace)
            defaultPlace
        } else {
            defaultPlace
        }
    }

    override suspend fun getAll(): List<Place> {
        return placeDao.getAll()
    }

    override suspend fun search(query: String): List<Place> {
        val response = try {
            placeService.search(
                userAgent = USER_AGENT,
                query = query,
                format = "jsonv2",
                acceptLanguage = Locale.getDefault().language
            )
        } catch (e: Exception) {
            listOf()
        }
        return withContext(dispatcherProvider.default) {
            response.map {
                Place(
                    id = it.id,
                    fullName = it.displayName,
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            }
        }
    }

    override suspend fun save(place: Place) {
        placeDao.insertOrUpdate(place)
    }

    override suspend fun isSaved(placeId: String): Boolean {
        return placeDao.get(placeId) != null
    }
}