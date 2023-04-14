package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Hour
import kotlin.time.Duration

internal interface ForecastRepository {
    suspend fun get(
        latitude: Double,
        longitude: Double,
        refreshIfOlderThan: Duration
    ): ForecastRepositoryResult

    suspend fun remove(latitude: Double, longitude: Double)
}

sealed interface ForecastRepositoryResult {
    data class Success(val data: List<Hour>): ForecastRepositoryResult
    object Empty : ForecastRepositoryResult
}