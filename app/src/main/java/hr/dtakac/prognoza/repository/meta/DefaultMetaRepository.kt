package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.MIN_DATE_TIME_RFC_1123
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.entity.ForecastMeta

class DefaultMetaRepository(
    private val metaDao: ForecastMetaDao
) : MetaRepository {
    override suspend fun get(): ForecastMeta? {
        return metaDao.get()
    }

    override suspend fun update(
        expiresTime: String?,
        lastModifiedTime: String?
    ) {
        metaDao.updateForecastMeta(
            ForecastMeta(
                expires = expiresTime ?: MIN_DATE_TIME_RFC_1123,
                lastModified = lastModifiedTime ?: MIN_DATE_TIME_RFC_1123
            )
        )
    }
}