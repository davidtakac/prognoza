package hr.dtakac.prognoza.domain.forecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum

interface ForecastProvider {
    suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult
}

sealed interface ForecastProviderResult {
    object Error : ForecastProviderResult
    data class Success(val data: List<ForecastDatum>): ForecastProviderResult
}