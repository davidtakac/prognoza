package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.mapping.mapDbModelToEntity
import hr.dtakac.prognoza.data.mapping.mapEntityToDbModel
import hr.dtakac.prognoza.data.mapping.mapResponseToEntity
import hr.dtakac.prognoza.data.network.place.PlaceService
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepositoryResult
import hr.dtakac.prognoza.entities.Place
import timber.log.Timber
import java.util.*

class DefaultPlaceRepository(
    private val placeService: PlaceService,
    private val placeDao: PlaceDao,
    private val userAgent: String
) : PlaceRepository {
    override suspend fun getSaved(latitude: Double, longitude: Double): Place? {
        return placeDao.get(latitude, longitude)?.let { mapDbModelToEntity(it) }
    }

    override suspend fun getSaved(): PlaceRepositoryResult {
        val entities = try {
            placeDao.getPlaces().map { mapDbModelToEntity(it) }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
        return entities?.let {
            PlaceRepositoryResult.Success(it)
        } ?: PlaceRepositoryResult.Error
    }

    override suspend fun search(query: String): PlaceRepositoryResult {
        val entities = try {
            placeService.search(
                userAgent = userAgent,
                acceptLanguage = Locale.getDefault().language,
                format = "jsonv2",
                query = query
            ).map(::mapResponseToEntity)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
        return entities?.let {
            PlaceRepositoryResult.Success(it)
        } ?: PlaceRepositoryResult.Error
    }

    override suspend fun save(place: Place) {
        placeDao.insert(mapEntityToDbModel(place))
    }
}