package hr.dtakac.prognoza.repository.meta

import hr.dtakac.prognoza.database.entity.ForecastMeta

interface MetaRepository {
    suspend fun updateSelectedPlaceMeta(
        expiresTime: String?,
        lastModifiedTime: String?
    )
    suspend fun getSelectedPlaceMeta(): ForecastMeta?
}