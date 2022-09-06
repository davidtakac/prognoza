package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.entities.forecast.Forecast
import java.lang.Exception
import java.time.ZonedDateTime

class GetForecast(
    private val getSelectedPlace: GetSelectedPlace,
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(
        from: ZonedDateTime,
        to: ZonedDateTime
    ): ForecastResult {
        val selectedPlace = getSelectedPlace()
        return try {
            forecastRepository.getForecast(
                latitude = selectedPlace.latitude,
                longitude = selectedPlace.longitude,
                from = from,
                to = to
            )
                .takeIf { it.isNotEmpty() }
                ?.let { ForecastResult.Success(it) }
                ?: ForecastResult.Empty
        } catch (e: Exception) {
            ForecastResult.Empty
        }
    }
}

sealed interface ForecastResult {
    data class Success(
        val forecast: List<Forecast>
    ): ForecastResult
    object Empty : ForecastResult
}