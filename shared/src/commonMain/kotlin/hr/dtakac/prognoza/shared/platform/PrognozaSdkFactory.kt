package hr.dtakac.prognoza.shared.platform

import com.squareup.sqldelight.db.SqlDriver
import hr.dtakac.prognoza.shared.PrognozaSdk
import hr.dtakac.prognoza.shared.data.PrognozaDatabase
import hr.dtakac.prognoza.shared.data.metnorway.*
import hr.dtakac.prognoza.shared.data.metnorway.network.MetNorwayForecastService
import hr.dtakac.prognoza.shared.data.openmeteo.CachedOpenMeteoResponseQueries
import hr.dtakac.prognoza.shared.data.openmeteo.OpenMeteoForecastProvider
import hr.dtakac.prognoza.shared.data.openmeteo.OpenMeteoMetaQueries
import hr.dtakac.prognoza.shared.data.openmeteo.network.OpenMeteoForecastService
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceSearcher
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceService
import hr.dtakac.prognoza.shared.data.prognoza.*
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
    private val sqlDriverFactory: SqlDriverFactory,
    private val dotDecimalFormatter: DotDecimalFormatter,
    private val rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser,
    private val ioDispatcher: CoroutineDispatcher,
    private val computationDispatcher: CoroutineDispatcher
) {
    fun create(): PrognozaSdk {
        val database = getDatabase(sqlDriverFactory.create())
        val metNorwayProvider = getMetNorwayForecastProvider(
            userAgent = userAgent,
            dotDecimalFormatter = dotDecimalFormatter,
            rfc1123UtcDateTimeParser = rfc1123UtcDateTimeParser,
            metaQueries = database.metaQueries,
            cachedResponseQueries = database.cachedResponseQueries,
            ioDispatcher = ioDispatcher,
            computationDispatcher = computationDispatcher
        )
        val openMeteoProvider = getOpenMeteoForecastProvider(
            userAgent = userAgent,
            dotDecimalFormatter = dotDecimalFormatter,
            computationDispatcher = computationDispatcher,
            ioDispatcher = ioDispatcher,
            metaQueries = database.openMeteoMetaQueries,
            cachedOpenMeteoResponseQueries = database.cachedOpenMeteoResponseQueries
        )
        val placeSearcher = getOsmPlaceSearcher(userAgent)
        val forecastRepository = DatabaseForecastRepository(
            forecastQueries = database.forecastQueries,
            metaQueries = database.prognozaMetaQueries,
            computationDispatcher = computationDispatcher,
            ioDispatcher = ioDispatcher
        )
        val placeRepository = DatabasePlaceRepository(
            placeQueries = database.placeQueries,
            ioDispatcher = ioDispatcher,
            computationDispatcher = computationDispatcher,
            forecastQueries = database.forecastQueries,
            metaQueries = database.prognozaMetaQueries
        )
        val settingsRepository = DatabaseSettingsRepository(
            settingsQueries = database.settingsQueries,
            savedPlaceGetter = placeRepository,
            ioDispatcher = ioDispatcher
        )
        val getSelectedPlace = GetSelectedPlace(settingsRepository)
        val getForecastProvider = GetForecastProvider(settingsRepository)
        val getTemperatureUnit = GetTemperatureUnit(settingsRepository)
        val getWindUnit = GetWindUnit(settingsRepository)
        val getPrecipitationUnit = GetPrecipitationUnit(settingsRepository)
        val actualGetForecast = ActualGetForecast(
            getSelectedPlace = getSelectedPlace,
            savedForecastGetter = forecastRepository,
            forecastSaver = forecastRepository,
            getForecastProvider = getForecastProvider,
            getTemperatureUnit = getTemperatureUnit,
            getPrecipitationUnit = getPrecipitationUnit,
            getWindUnit = getWindUnit,
            openMeteoProvider = openMeteoProvider,
            metNorwayProvider = metNorwayProvider
        )

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
            override val getForecast = GetForecastLatest(actualGetForecast)
            override val getForecastFrugal = GetForecastFrugal(actualGetForecast)
            override val deleteSavedPlace = DeleteSavedPlace(placeRepository)
            override val getForecastProvider = getForecastProvider
            override val setForecastProvider = SetForecastProvider(settingsRepository)
            override val getAllForecastProviders = GetAllForecastProviders()

            override val searchPlaces = SearchPlaces(
                placeSearcher = placeSearcher,
                localRfc2616LanguageGetter = localRfc2616LanguageGetter
            )

            override val selectPlace = SelectPlace(
                placeSaver = placeRepository,
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

    private fun getDatabase(sqlDriver: SqlDriver): PrognozaDatabase = PrognozaDatabase(
        driver = sqlDriver,
        ForecastAdapter = Forecast.Adapter(
            temperatureAdapter = temperatureSqlAdapter,
            descriptionAdapter = descriptionSqlAdapter,
            moodAdapter = moodSqlAdapter,
            precipitationAdapter = lengthSqlAdapter,
            windSpeedAdapter = speedSqlAdapter,
            windFromDirectionAdapter = angleSqlAdapter,
            humidityAdapter = percentageSqlAdapter,
            airPressureAtSeaLevelAdapter = pressureSqlAdapter
        ),
        SettingsAdapter = Settings.Adapter(
            temperatureUnitAdapter = temperatureUnitSqlAdapter,
            precipitationUnitAdapter = lengthUnitSqlAdapter,
            windUnitAdapter = speedUnitSqlAdapter,
            pressureUnitAdapter = pressureUnitSqlAdapter,
            forecastProviderAdapter = forecastProviderAdapter
        )
    )

    private fun getMetNorwayForecastProvider(
        userAgent: String,
        dotDecimalFormatter: DotDecimalFormatter,
        rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser,
        metaQueries: MetaQueries,
        cachedResponseQueries: CachedResponseQueries,
        ioDispatcher: CoroutineDispatcher,
        computationDispatcher: CoroutineDispatcher
    ): MetNorwayForecastProvider = MetNorwayForecastProvider(
        apiService = MetNorwayForecastService(
            client = getHttpClient(),
            userAgent = userAgent,
            baseUrl = "https://api.met.no/weatherapi",
            dotDecimalFormatter = dotDecimalFormatter,
            epochMillisToRfc1123 = rfc1123UtcDateTimeParser::format
        ),
        metaQueries = metaQueries,
        cachedResponseQueries = cachedResponseQueries,
        ioDispatcher = ioDispatcher,
        computationDispatcher = computationDispatcher,
        rfc1123ToEpochMillis = rfc1123UtcDateTimeParser::parseToEpochMillis
    )

    private fun getOpenMeteoForecastProvider(
        userAgent: String,
        dotDecimalFormatter: DotDecimalFormatter,
        computationDispatcher: CoroutineDispatcher,
        ioDispatcher: CoroutineDispatcher,
        metaQueries: OpenMeteoMetaQueries,
        cachedOpenMeteoResponseQueries: CachedOpenMeteoResponseQueries
    ): OpenMeteoForecastProvider = OpenMeteoForecastProvider(
        apiService = OpenMeteoForecastService(
            client = getHttpClient(),
            userAgent = userAgent,
            baseUrl = "https://api.open-meteo.com/v1",
            dotDecimalFormatter = dotDecimalFormatter
        ),
        computationDispatcher = computationDispatcher,
        ioDispatcher = ioDispatcher,
        cachedResponseQueries = cachedOpenMeteoResponseQueries,
        metaQueries = metaQueries
    )

    private fun getOsmPlaceSearcher(userAgent: String): OsmPlaceSearcher = OsmPlaceSearcher(
        osmPlaceService = OsmPlaceService(
            client = getHttpClient(),
            baseUrl = "https://nominatim.openstreetmap.org",
            userAgent = userAgent
        )
    )
}