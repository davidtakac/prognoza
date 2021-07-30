package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.MIN_DATE_TIME_RFC_1123
import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
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
                expires = ForecastMetaDateTimeConverter.fromTimestamp(expiresTime ?: MIN_DATE_TIME_RFC_1123)!!,
                lastModified = ForecastMetaDateTimeConverter.fromTimestamp(lastModifiedTime ?: MIN_DATE_TIME_RFC_1123)!!,
                placeId = preferencesRepository.getSelectedPlaceId()
            )
        )
    }
}