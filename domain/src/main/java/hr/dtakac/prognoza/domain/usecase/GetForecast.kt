package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepositoryResult
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import hr.dtakac.prognoza.entities.forecast.Forecast

class GetForecast(
    private val getSelectedPlace: GetSelectedPlace,
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): GetForecastResult {
        val selectedPlace = getSelectedPlace() ?: return GetForecastResult.Empty.NoSelectedPlace
        val from = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS)
        val to = from.plusDays(7L)
        return forecastRepository.getForecast(
            latitude = selectedPlace.latitude,
            longitude = selectedPlace.longitude,
            from = from,
            to = to
        ).let { mapToResult(selectedPlace, it) }
    }

    private suspend fun mapToResult(
        place: Place,
        repositoryResult: ForecastRepositoryResult
    ): GetForecastResult = when (repositoryResult) {
        is ForecastRepositoryResult.Empty -> GetForecastResult.Empty.Error
        is ForecastRepositoryResult.Success -> {
            GetForecastResult.Success(
                placeName = place.name,
                forecast = repositoryResult.data,
                temperatureUnit = settingsRepository.getTemperatureUnit(),
                windUnit = settingsRepository.getWindUnit(),
                precipitationUnit = settingsRepository.getPrecipitationUnit()
            )
        }
    }
}

sealed interface GetForecastResult {
    data class Success(
        val placeName: String,
        val forecast: Forecast,
        val temperatureUnit: TemperatureUnit,
        val windUnit: SpeedUnit,
        val precipitationUnit: LengthUnit
    ) : GetForecastResult

    sealed interface Empty : GetForecastResult {
        object Error : Empty
        object NoSelectedPlace : Empty
    }
}