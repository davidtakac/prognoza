package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Forecast

internal interface ForecastRepository {
    suspend fun get(placeId: String): Forecast?
    suspend fun delete(placeId: String)
}