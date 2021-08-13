package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.entity.ForecastMeta

class DefaultMetaRepository(
    private val metaDao: ForecastMetaDao,
) : MetaRepository {
    override suspend fun get(placeId: String): ForecastMeta? {
        return metaDao.get(placeId)
    }

    override suspend fun update(
        placeId: String,
        expiresTime: String?,
        lastModifiedTime: String?
    ) {
        metaDao.updateForecastMeta(
            ForecastMeta(
                expires = ForecastMetaDateTimeConverter.fromTimestamp(expiresTime),
                lastModified = ForecastMetaDateTimeConverter.fromTimestamp(lastModifiedTime),
                placeId = placeId
            )
        )
    }
}