package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.forecast.Forecast
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        from: ZonedDateTime,
        to: ZonedDateTime
    ): ForecastRepositoryResult
}

sealed interface ForecastRepositoryResult {
    object Empty : ForecastRepositoryResult
    data class Success(
        val data: Forecast
    ) : ForecastRepositoryResult
}