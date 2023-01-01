package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.*
import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.shared.entity.ForecastProvider
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

interface GetForecast {
    suspend operator fun invoke(): GetForecastResult
}

internal class GetForecastLatest(
    private val getForecast: ActualGetForecast
) : GetForecast {
    override suspend operator fun invoke(): GetForecastResult =
        getForecast.get(refreshIfOlderThan = Duration.ZERO)
}

internal class GetForecastFrugal(
    private val getForecast: ActualGetForecast
) : GetForecast {
    override suspend fun invoke(): GetForecastResult =
        getForecast.get(refreshIfOlderThan = Duration.parse("PT6H"))
}

internal class ActualGetForecast internal constructor(
    private val getSelectedPlace: GetSelectedPlace,
    private val getForecastProvider: GetForecastProvider,
    private val getPrecipitationUnit: GetPrecipitationUnit,
    private val getWindUnit: GetWindUnit,
    private val getTemperatureUnit: GetTemperatureUnit,
    private val forecastRepository: ForecastRepository
) {
    internal suspend fun get(
        refreshIfOlderThan: Duration = Duration.ZERO
    ): GetForecastResult {
        val selectedPlace = getSelectedPlace() ?: return GetForecastResult.Empty.NoSelectedPlace
        val data = forecastRepository.get(
            latitude = selectedPlace.latitude,
            longitude = selectedPlace.longitude,
            from = getForecastProvider(),
            refreshIfOlderThan = refreshIfOlderThan
        )
        return mapToResult(
            place = selectedPlace,
            data = (data as? ForecastRepositoryResult.Success)?.data ?: listOf(),
            provider = (data as? ForecastRepositoryResult.Success)?.provider ?: ForecastProvider.MET_NORWAY
        )
    }

    private suspend fun mapToResult(
        place: Place,
        data: List<ForecastDatum>,
        provider: ForecastProvider
    ): GetForecastResult = if (data.isEmpty()) {
        GetForecastResult.Empty.Error
    } else GetForecastResult.Success(
        placeName = place.name,
        forecast = Forecast(data, TimeZone.currentSystemDefault()),
        provider = provider,
        temperatureUnit = getTemperatureUnit(),
        windUnit = getWindUnit(),
        precipitationUnit = getPrecipitationUnit()
    )
}

sealed interface GetForecastResult {
    data class Success(
        val placeName: String,
        val forecast: Forecast,
        val provider: ForecastProvider,
        val temperatureUnit: TemperatureUnit,
        val windUnit: SpeedUnit,
        val precipitationUnit: LengthUnit
    ) : GetForecastResult

    sealed interface Empty : GetForecastResult {
        object Error : Empty
        object NoSelectedPlace : Empty
    }
}