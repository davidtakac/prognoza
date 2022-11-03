package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.PrognozaDatabase
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.place.PlaceSaver
import hr.dtakac.prognoza.entities.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PlaceRepository(
    private val database: PrognozaDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val computationDispatcher: CoroutineDispatcher
) : SavedPlaceGetter, PlaceSaver {
    override suspend fun get(latitude: Double, longitude: Double): Place? {
        return withContext(ioDispatcher) {
            database.placeQueries
                .get(latitude, longitude)
                .executeAsOneOrNull()
                ?.let {
                    Place(
                        name = it.name,
                        details = it.details,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
        }
    }

    override suspend fun getAll(): List<Place> {
        val placeDbModels = withContext(ioDispatcher) {
            database.placeQueries
                .getAll()
                .executeAsList()
        }
        return withContext(computationDispatcher) {
            placeDbModels
                .map {
                    Place(
                        name = it.name,
                        details = it.details,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
        }
    }

    override suspend fun save(place: Place) {
        withContext(ioDispatcher) {
            database.placeQueries.insert(
                latitude = place.latitude,
                longitude = place.longitude,
                name = place.name,
                details = place.details
            )
        }
    }
}