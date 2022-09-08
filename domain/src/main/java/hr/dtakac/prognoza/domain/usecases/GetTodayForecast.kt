package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepositoryResult
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.DayForecast
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class GetTodayForecast(
    private val getSelectedPlace: GetSelectedPlace,
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): TodayForecastResult {
        val selectedPlace = getSelectedPlace() ?: return TodayForecastResult.NoSelectedPlace
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
            is ForecastRepositoryResult.ThrottleError -> TodayForecastResult.ThrottleError
            is ForecastRepositoryResult.ClientError -> TodayForecastResult.ClientError
            is ForecastRepositoryResult.ServerError -> TodayForecastResult.ServerError
            is ForecastRepositoryResult.UnknownError -> TodayForecastResult.UnknownError
            is ForecastRepositoryResult.DatabaseError -> TodayForecastResult.DatabaseError
            is ForecastRepositoryResult.Success -> {
                if (repositoryResult.data.isEmpty()) TodayForecastResult.UnknownError
                else TodayForecastResult.Success(
                    placeName = place.name,
                    todayForecast = DayForecast(repositoryResult.data),
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
        val todayForecast: DayForecast,
        val temperatureUnit: TemperatureUnit,
        val windUnit: SpeedUnit,
        val precipitationUnit: LengthUnit
    ) : TodayForecastResult

    object NoSelectedPlace : TodayForecastResult
    object ThrottleError : TodayForecastResult
    object ClientError : TodayForecastResult
    object ServerError : TodayForecastResult
    object DatabaseError : TodayForecastResult
    object UnknownError : TodayForecastResult
}