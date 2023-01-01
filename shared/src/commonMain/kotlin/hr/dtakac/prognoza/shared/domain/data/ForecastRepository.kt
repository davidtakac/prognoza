package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.ForecastDatum
import kotlin.time.Duration

internal interface ForecastRepository {
    suspend fun get(
        latitude: Double,
        longitude: Double,
        from: hr.dtakac.prognoza.shared.entity.ForecastProvider,
        refreshIfOlderThan: Duration
    ): ForecastRepositoryResult
}

sealed interface ForecastRepositoryResult {
    data class Success(
        val data: List<ForecastDatum>,
        val provider: hr.dtakac.prognoza.shared.entity.ForecastProvider
    ): ForecastRepositoryResult

    object Empty : ForecastRepositoryResult
}