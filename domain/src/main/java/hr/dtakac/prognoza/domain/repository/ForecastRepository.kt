package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import java.time.ZonedDateTime

interface ForecastRepository {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        from: ZonedDateTime,
        to: ZonedDateTime
    ): List<ForecastDatum>
}