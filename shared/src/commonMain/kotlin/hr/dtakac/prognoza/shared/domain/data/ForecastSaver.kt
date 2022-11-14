package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.ForecastDatum

interface ForecastSaver {
    suspend fun save(
        latitude: Double,
        longitude: Double,
        data: List<ForecastDatum>
    )
}