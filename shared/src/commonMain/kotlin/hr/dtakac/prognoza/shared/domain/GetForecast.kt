package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.ForecastRepository
import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.Forecast

class GetForecast internal constructor(
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): ForecastResult {
        val placeId = settingsRepository.getSelectedPlaceId() ?: return ForecastResult.NoPlace
        val forecast = forecastRepository.getForecast(placeId) ?: return ForecastResult.Failure
        return ForecastResult.Success(forecast)
    }
}

sealed interface ForecastResult {
    object NoPlace : ForecastResult
    object Failure : ForecastResult
    data class Success(val forecast: Forecast) : ForecastResult
}