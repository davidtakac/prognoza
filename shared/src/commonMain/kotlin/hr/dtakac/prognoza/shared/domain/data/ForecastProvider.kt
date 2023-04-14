package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Hour

internal interface ForecastProvider {
    suspend fun provide(
        latitude: Double,
        longitude: Double
    ): ForecastProviderResult
}

internal sealed interface ForecastProviderResult {
    object Error : ForecastProviderResult
    data class Success(val data: List<Hour>): ForecastProviderResult
}