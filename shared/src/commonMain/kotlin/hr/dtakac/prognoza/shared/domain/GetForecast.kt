package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.*
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.TimeZone

class GetForecast internal constructor(
    private val getSelectedPlace: GetSelectedPlace,
    private val savedForecastGetter: SavedForecastGetter,
    private val forecastSaver: ForecastSaver,
    private val forecastProvider: ForecastProvider,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): GetForecastResult {
        val selectedPlace = getSelectedPlace() ?: return GetForecastResult.Empty.NoSelectedPlace
        val latitude = selectedPlace.latitude
        val longitude = selectedPlace.longitude

        val freshForecast = forecastProvider.provide(latitude, longitude)
        if (freshForecast is ForecastProviderResult.Success) {
            forecastSaver.save(latitude, longitude, freshForecast.data)
        }

        return mapToResult(
            selectedPlace,
            savedForecastGetter.get(latitude, longitude)
        )
    }

    private suspend fun mapToResult(
        place: Place,
        data: List<ForecastDatum>
    ): GetForecastResult = if (data.isEmpty()) {
        GetForecastResult.Empty.Error
    } else GetForecastResult.Success(
        placeName = place.name,
        forecast = Forecast(data, TimeZone.currentSystemDefault()),
        temperatureUnit = settingsRepository.getTemperatureUnit(),
        windUnit = settingsRepository.getWindUnit(),
        precipitationUnit = settingsRepository.getPrecipitationUnit()
    )
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