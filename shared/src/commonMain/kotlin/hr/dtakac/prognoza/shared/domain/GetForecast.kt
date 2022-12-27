package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.*
import hr.dtakac.prognoza.shared.domain.data.ForecastProvider
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.Clock
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
    private val savedForecastGetter: SavedForecastGetter,
    private val forecastSaver: ForecastSaver,
    private val openMeteoProvider: ForecastProvider,
    private val metNorwayProvider: ForecastProvider
) {
    internal suspend fun get(
        refreshIfOlderThan: Duration = Duration.ZERO
    ): GetForecastResult {
        val selectedPlace = getSelectedPlace() ?: return GetForecastResult.Empty.NoSelectedPlace
        val latitude = selectedPlace.latitude
        val longitude = selectedPlace.longitude
        val shouldPullFromProvider = shouldPullFromProvider(
            refreshIfOlderThan = refreshIfOlderThan,
            latitude = latitude,
            longitude = longitude
        )

        if (shouldPullFromProvider) {
            val freshForecast = getProvider().provide(latitude, longitude)
            if (freshForecast is ForecastProviderResult.Success) {
                forecastSaver.save(latitude, longitude, freshForecast.data)
            }
        }

        return mapToResult(
            place = selectedPlace,
            data = (savedForecastGetter.get(
                latitude,
                longitude
            ) as? SavedForecastGetterResult.Success)
                ?.data
                ?: listOf()
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
        provider = getForecastProvider(),
        temperatureUnit = getTemperatureUnit(),
        windUnit = getWindUnit(),
        precipitationUnit = getPrecipitationUnit()
    )

    private suspend fun shouldPullFromProvider(
        refreshIfOlderThan: Duration,
        latitude: Double,
        longitude: Double
    ): Boolean {
        return if (refreshIfOlderThan > Duration.ZERO) {
            val lastUpdated = (savedForecastGetter.get(
                latitude = latitude,
                longitude = longitude
            ) as? SavedForecastGetterResult.Success)?.lastUpdatedEpochMillis ?: 0L
            Clock.System.now().toEpochMilliseconds() - lastUpdated >= refreshIfOlderThan.inWholeMilliseconds
        } else true
    }

    private suspend fun getProvider(): ForecastProvider = when (getForecastProvider()) {
        hr.dtakac.prognoza.shared.entity.ForecastProvider.OPEN_METEO -> openMeteoProvider
        hr.dtakac.prognoza.shared.entity.ForecastProvider.MET_NORWAY -> metNorwayProvider
    }
}

sealed interface GetForecastResult {
    data class Success(
        val placeName: String,
        val forecast: Forecast,
        val provider: hr.dtakac.prognoza.shared.entity.ForecastProvider,
        val temperatureUnit: TemperatureUnit,
        val windUnit: SpeedUnit,
        val precipitationUnit: LengthUnit
    ) : GetForecastResult

    sealed interface Empty : GetForecastResult {
        object Error : Empty
        object NoSelectedPlace : Empty
    }
}