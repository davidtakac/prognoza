package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.ForecastDatum

internal interface SavedForecastGetter {
    suspend fun get(
        latitude: Double,
        longitude: Double
    ): SavedForecastGetterResult
}

sealed class SavedForecastGetterResult {
    object Empty : SavedForecastGetterResult()
    data class Success(
        val data: List<ForecastDatum>,
        val lastUpdatedEpochMillis: Long
    ) : SavedForecastGetterResult()
}