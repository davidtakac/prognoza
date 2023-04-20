package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.Forecast

class ForecastRepository {
    suspend fun get(placeId: String): Forecast? { return null }
    suspend fun delete(placeId: String) {/*todo*/}
}