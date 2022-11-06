package hr.dtakac.prognoza.data.repository

import hr.dtakac.prognoza.data.PlaceQueries
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.place.PlaceSaver
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.data.Place as PlaceDbModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PlaceRepository(
    private val placeQueries: PlaceQueries,
    private val ioDispatcher: CoroutineDispatcher,
    private val computationDispatcher: CoroutineDispatcher
) : SavedPlaceGetter, PlaceSaver {
    override suspend fun get(latitude: Double, longitude: Double): Place? {
        return withContext(ioDispatcher) {
            placeQueries
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
            placeQueries
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
            placeQueries.insert(
                PlaceDbModel(
                    latitude = place.latitude,
                    longitude = place.longitude,
                    name = place.name,
                    details = place.details
                )
            )
        }
    }
}