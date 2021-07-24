package hr.dtakac.prognoza.repository.location

import hr.dtakac.prognoza.database.dao.ForecastLocationDao
import hr.dtakac.prognoza.database.entity.ForecastLocation
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository

class DefaultLocationRepository(
    private val locationDao: ForecastLocationDao,
    private val preferencesRepository: PreferencesRepository
) : LocationRepository {
    override suspend fun getSelectedLocation(): ForecastLocation {
        return locationDao.get(preferencesRepository.locationId)
            // todo: throw exception if not initialized
            ?: ForecastLocation(1, "Osijek", "31000", "Croatia", 45f, 18f)
    }
}