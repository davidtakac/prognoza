package hr.dtakac.prognoza.domain.forecast

import hr.dtakac.prognoza.entities.forecast.ForecastDatum

interface ForecastSaver {
    suspend fun save(
        latitude: Double,
        longitude: Double,
        data: List<ForecastDatum>
    )
}