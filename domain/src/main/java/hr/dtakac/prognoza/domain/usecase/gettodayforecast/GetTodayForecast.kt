package hr.dtakac.prognoza.domain.usecase.gettodayforecast

import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepositoryResult
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.domain.usecase.GetSelectedPlace
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecastResult.*

class GetTodayForecast(
    private val getSelectedPlace: GetSelectedPlace,
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): TodayForecastResult {
        val selectedPlace = getSelectedPlace() ?: return Error.NoSelectedPlace
        val now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS)
        return forecastRepository.getForecast(
            latitude = selectedPlace.latitude,
            longitude = selectedPlace.longitude,
            from = now,
            to = now.plusHours(23L)
        ).let { mapToResult(selectedPlace, it) }
    }

    private suspend fun mapToResult(place: Place, repositoryResult: ForecastRepositoryResult): TodayForecastResult =
        when (repositoryResult) {
            is ForecastRepositoryResult.ThrottleError -> Error.Throttle
            is ForecastRepositoryResult.ClientError -> Error.Client
            is ForecastRepositoryResult.ServerError -> Error.Server
            is ForecastRepositoryResult.UnknownError -> Error.Unknown
            is ForecastRepositoryResult.DatabaseError -> Error.Database
            is ForecastRepositoryResult.Success -> {
                if (repositoryResult.data.isEmpty()) Error.Unknown
                else Success(
                    placeName = place.name,
                    todayForecast = TodayForecast(repositoryResult.data),
                    temperatureUnit = settingsRepository.getTemperatureUnit(),
                    windUnit = settingsRepository.getWindUnit(),
                    precipitationUnit = settingsRepository.getPrecipitationUnit()
                )
            }
        }
}

sealed interface TodayForecastResult {
    data class Success(
        val placeName: String,
        val todayForecast: TodayForecast,
        val temperatureUnit: TemperatureUnit,
        val windUnit: SpeedUnit,
        val precipitationUnit: LengthUnit
    ) : TodayForecastResult

    sealed interface Error : TodayForecastResult {
        object NoSelectedPlace : Error
        object Throttle : Error
        object Client : Error
        object Server : Error
        object Database : Error
        object Unknown : Error
    }
}