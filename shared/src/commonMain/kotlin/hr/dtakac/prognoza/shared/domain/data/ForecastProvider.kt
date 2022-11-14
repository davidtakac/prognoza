package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.ForecastDatum

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