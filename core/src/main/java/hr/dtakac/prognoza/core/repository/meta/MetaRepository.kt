package hr.dtakac.prognoza.core.repository.meta

import hr.dtakac.prognoza.core.model.database.ForecastMeta

interface MetaRepository {
    suspend fun update(
        placeId: String,
        expiresTime: String?,
        lastModifiedTime: String?
    )

    suspend fun get(placeId: String): ForecastMeta?
}