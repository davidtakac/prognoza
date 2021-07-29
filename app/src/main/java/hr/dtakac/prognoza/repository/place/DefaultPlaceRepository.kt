package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository

class DefaultPlaceRepository(
    private val placeDao: PlaceDao,
    private val preferencesRepository: PreferencesRepository
) : PlaceRepository {
    override suspend fun getSelectedPlace(): Place {
        return placeDao.get(preferencesRepository.placeId)
            // todo: throw exception if not initialized
            ?: Place(1, "Osijek", "31000", "Croatia", 45f, 18f)
    }
}