package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.data.database.converter.ForecastMetaDateTimeConverter
import hr.dtakac.prognoza.data.database.forecast.ForecastMetaDao
import hr.dtakac.prognoza.data.database.forecast.ForecastMeta

class DefaultMetaRepository(
    private val metaDao: hr.dtakac.prognoza.data.database.forecast.ForecastMetaDao,
) : MetaRepository {
    override suspend fun get(placeId: String): hr.dtakac.prognoza.data.database.forecast.ForecastMeta? {
        return metaDao.get(placeId)
    }

    override suspend fun update(
        placeId: String,
        expiresTime: String?,
        lastModifiedTime: String?
    ) {
        metaDao.updateForecastMeta(
            hr.dtakac.prognoza.data.database.forecast.ForecastMeta(
                expires = hr.dtakac.prognoza.data.database.converter.ForecastMetaDateTimeConverter.fromTimestamp(expiresTime),
                lastModified = hr.dtakac.prognoza.data.database.converter.ForecastMetaDateTimeConverter.fromTimestamp(lastModifiedTime),
                placeId = placeId
            )
        )
    }
}