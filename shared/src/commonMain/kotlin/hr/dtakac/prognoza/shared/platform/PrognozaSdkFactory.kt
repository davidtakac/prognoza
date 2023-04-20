package hr.dtakac.prognoza.shared.platform

import hr.dtakac.prognoza.shared.PrognozaSdk
import hr.dtakac.prognoza.shared.data.*
import hr.dtakac.prognoza.shared.data.PlaceService
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
        val placeProvider = PlaceService(
            client = getHttpClient(),
            userAgent = userAgent,
            computationDispatcher = computationDispatcher
        )
        val forecastRepository = ForecastRepository()
        val placeRepository = PlaceRepository()
        val settingsRepository = SettingsRepository()
        val getSelectedPlace = GetSelectedPlace(settingsRepository)
        val getTemperatureUnit = GetTemperatureUnit(settingsRepository)
        val getWindUnit = GetWindUnit(settingsRepository)
        val getPrecipitationUnit = GetPrecipitationUnit(settingsRepository)

        return object : PrognozaSdk {
            override val getAllPrecipitationUnits = GetAllPrecipitationUnits()
            override val getAllPressureUnits = GetAllPressureUnits()
            override val getAllTemperatureUnits = GetAllTemperatureUnits()
            override val getAllWindUnits = GetAllWindUnits()
            override val getPrecipitationUnit = GetPrecipitationUnit(settingsRepository)
            override val getPressureUnit = GetPressureUnit(settingsRepository)
            override val getSavedPlaces = GetSavedPlaces(placeRepository)
            override val getSelectedPlace = getSelectedPlace
            override val getTemperatureUnit = GetTemperatureUnit(settingsRepository)
            override val getWindUnit = GetWindUnit(settingsRepository)
            override val setPrecipitationUnit = SetPrecipitationUnit(settingsRepository)
            override val setPressureUnit = SetPressureUnit(settingsRepository)
            override val setTemperatureUnit = SetTemperatureUnit(settingsRepository)
            override val setWindUnit = SetWindUnit(settingsRepository)
            override val deleteSavedPlace = DeleteSavedPlace(placeRepository, forecastRepository)
            override val searchPlaces = SearchPlaces(placeRepository)
            override val selectPlace = SelectPlace(
                placeRepository = placeRepository,
                settingsRepository = settingsRepository
            )
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