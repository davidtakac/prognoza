package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.database.entity.ForecastMeta

interface MetaRepository {
    suspend fun update(
        placeId: String,
        expiresTime: String?,
        lastModifiedTime: String?
    )
    suspend fun get(placeId: String): ForecastMeta?
}