package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.database.entity.ForecastMeta

interface MetaRepository {
    suspend fun update(
        expiresTime: String?,
        lastModifiedTime: String?
    )
    suspend fun get(): ForecastMeta?
}