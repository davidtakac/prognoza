package hr.dtakac.prognoza.domain.forecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum

interface SavedForecastGetter {
    suspend fun get(
        latitude: Double,
        longitude: Double
    ): List<ForecastDatum>
}