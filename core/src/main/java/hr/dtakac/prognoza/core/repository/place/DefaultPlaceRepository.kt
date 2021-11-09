package hr.dtakac.prognoza.core.repository.place

import hr.dtakac.prognoza.core.api.PlaceService
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.database.dao.PlaceDao
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.utils.USER_AGENT
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