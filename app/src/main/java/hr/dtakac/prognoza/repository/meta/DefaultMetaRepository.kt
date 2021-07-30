package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.MIN_DATE_TIME_RFC_1123
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository

class DefaultMetaRepository(
    private val metaDao: ForecastMetaDao,
    private val preferencesRepository: PreferencesRepository
) : MetaRepository {
    override suspend fun getSelectedPlaceMeta(): ForecastMeta? {
        return metaDao.get(preferencesRepository.getSelectedPlaceId())
    }

    override suspend fun updateSelectedPlaceMeta(
        expiresTime: String?,
        lastModifiedTime: String?
    ) {
        metaDao.updateForecastMeta(
            ForecastMeta(
                expires = expiresTime ?: MIN_DATE_TIME_RFC_1123,
                lastModified = lastModifiedTime ?: MIN_DATE_TIME_RFC_1123,
                placeId = preferencesRepository.getSelectedPlaceId()
            )
        )
    }
}