package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.common.MIN_DATE_TIME_RFC_1123
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
                expires = ForecastMetaDateTimeConverter.fromTimestamp(
                    expiresTime ?: MIN_DATE_TIME_RFC_1123
                )!!,
                lastModified = ForecastMetaDateTimeConverter.fromTimestamp(
                    lastModifiedTime ?: MIN_DATE_TIME_RFC_1123
                )!!,
                placeId = placeId
            )
        )
    }
}