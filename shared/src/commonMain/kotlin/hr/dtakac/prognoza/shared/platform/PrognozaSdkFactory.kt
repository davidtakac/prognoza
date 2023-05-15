package hr.dtakac.prognoza.shared.platform

import hr.dtakac.prognoza.shared.PrognozaSdk
import hr.dtakac.prognoza.shared.data.*
import hr.dtakac.prognoza.shared.domain.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

expect class PrognozaSdkFactory {
    fun create(): PrognozaSdk
}

internal class InternalPrognozaSdkFactory constructor(
    private val userAgent: String,
    private val localRfc2616LanguageGetter: LocalRfc2616LanguageGetter,
    private val dotDecimalFormatter: DotDecimalFormatter,
    private val rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser,
    private val ioDispatcher: CoroutineDispatcher,
    private val computationDispatcher: CoroutineDispatcher
) {
    fun create(): PrognozaSdk {
        val apiService = ForecastService(
            client = getHttpClient(),
            userAgent = userAgent,
            dotDecimalFormatter = dotDecimalFormatter,
            computationDispatcher = computationDispatcher
        )
        val placeRepository = PlaceRepository()
        val forecastRepository = ForecastRepository(apiService, placeRepository)
        val settingsRepository = SettingsRepository()
        val getSelectedPlace = GetSelectedPlace(settingsRepository, placeRepository)
        val getForecast = GetForecast(forecastRepository, settingsRepository)

        return object : PrognozaSdk {
            override val getOverview: GetOverview = GetOverview(getForecast, computationDispatcher)
            override val getAvailableLengthUnits = GetAvailableLengthUnits()
            override val getAvailablePressureUnits = GetAvailablePressureUnits()
            override val getAvailableTemperatureUnits = GetAvailableTemperatureUnits()
            override val getAvailableWindSpeedUnits = GetAvailableWindSpeedUnits()
            override val getSelectedLengthUnit = GetSelectedLengthUnit(settingsRepository)
            override val getSelectedPressureUnit = GetSelectedPressureUnit(settingsRepository)
            override val getSavedPlaces = GetSavedPlaces(placeRepository)
            override val getSelectedPlace = getSelectedPlace
            override val getSelectedTemperatureUnit = GetSelectedTemperatureUnit(settingsRepository)
            override val getSelectedWindSpeedUnit = GetSelectedWindSpeedUnit(settingsRepository)
            override val selectLengthUnit = SelectLengthUnit(settingsRepository)
            override val selectPressureUnit = SelectPressureUnit(settingsRepository)
            override val selectTemperatureUnit = SelectTemperatureUnit(settingsRepository)
            override val selectWindSpeedUnit = SelectWindSpeedUnit(settingsRepository)
            override val deleteSavedPlace = DeleteSavedPlace(placeRepository, forecastRepository)
            override val searchPlaces = SearchPlaces(placeRepository)
            override val selectPlace = SelectPlace(placeRepository, settingsRepository)
        }
    }

    private fun getHttpClient(): HttpClient = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}