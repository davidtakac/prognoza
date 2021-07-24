package hr.dtakac.prognoza.repository.location

import hr.dtakac.prognoza.database.entity.ForecastLocation

interface LocationRepository {
    suspend fun getSelectedLocation(): ForecastLocation
}