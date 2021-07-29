package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository

class DefaultPlaceRepository(
    private val placeDao: PlaceDao,
    private val preferencesRepository: PreferencesRepository
) : PlaceRepository {
    override suspend fun getSelectedPlace(): Place {
        val place = placeDao.get(preferencesRepository.placeId)
        return if (place == null) {
            val defaultPlace = Place(
                id = DEFAULT_PLACE_ID,
                name = "Osijek",
                fullName = "Osijek, Grad Osijek, Osijek-Baranja County, Croatia",
                latitude = 45.55f,
                longitude = 18.69f
            )
            placeDao.insertOrUpdate(defaultPlace)
            defaultPlace
        } else {
            place
        }
    }
}