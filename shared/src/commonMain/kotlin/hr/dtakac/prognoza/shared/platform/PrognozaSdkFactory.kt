package hr.dtakac.prognoza.shared.platform

import com.squareup.sqldelight.db.SqlDriver
import hr.dtakac.prognoza.shared.data.PrognozaDatabase
import hr.dtakac.prognoza.shared.data.metnorway.*
import hr.dtakac.prognoza.shared.data.metnorway.network.MetNorwayForecastService
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceSearcher
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceService
import hr.dtakac.prognoza.shared.data.prognoza.*
import hr.dtakac.prognoza.shared.domain.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

open class PrognozaSdkFactory internal constructor(
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
        val forecastProvider = getMetNorwayForecastProvider(
            userAgent = userAgent,
            dotDecimalFormatter = dotDecimalFormatter,
            rfc1123UtcDateTimeParser = rfc1123UtcDateTimeParser,
            metaQueries = database.metaQueries,
            cachedResponseQueries = database.cachedResponseQueries,
            ioDispatcher = ioDispatcher
        )
        val placeSearcher = getOsmPlaceSearcher(userAgent)
        val forecastRepository = DatabaseForecastRepository(
            forecastQueries = database.forecastQueries,
            computationDispatcher = computationDispatcher,
            ioDispatcher = ioDispatcher
        )
        val placeRepository = DatabasePlaceRepository(
            placeQueries = database.placeQueries,
            ioDispatcher = ioDispatcher,
            computationDispatcher = computationDispatcher
        )
        val settingsRepository = DatabaseSettingsRepository(
            settingsQueries = database.settingsQueries,
            savedPlaceGetter = placeRepository,
            ioDispatcher = ioDispatcher
        )

        return object : PrognozaSdk {
            override val getAllPrecipitationUnits = GetAllPrecipitationUnits()
            override val getAllPressureUnits = GetAllPressureUnits()
            override val getAllTemperatureUnits = GetAllTemperatureUnits()
            override val getAllWindUnits = GetAllWindUnits()
            override val getPrecipitationUnit = GetPrecipitationUnit(settingsRepository)
            override val getPressureUnit = GetPressureUnit(settingsRepository)
            override val getSavedPlaces = GetSavedPlaces(placeRepository)
            override val getSelectedPlace = GetSelectedPlace(settingsRepository)
            override val getTemperatureUnit = GetTemperatureUnit(settingsRepository)
            override val getWindUnit = GetWindUnit(settingsRepository)
            override val setPrecipitationUnit = SetPrecipitationUnit(settingsRepository)
            override val setPressureUnit = SetPressureUnit(settingsRepository)
            override val setTemperatureUnit = SetTemperatureUnit(settingsRepository)
            override val setWindUnit = SetWindUnit(settingsRepository)

            override val searchPlaces = SearchPlaces(
                placeSearcher = placeSearcher,
                localRfc2616LanguageGetter = localRfc2616LanguageGetter
            )

            override val selectPlace = SelectPlace(
                placeSaver = placeRepository,
                settingsRepository = settingsRepository
            )

            override val getForecast = GetForecast(
                getSelectedPlace = getSelectedPlace,
                savedForecastGetter = forecastRepository,
                forecastSaver = forecastRepository,
                forecastProvider = forecastProvider,
                settingsRepository = settingsRepository
            )
        }
    }

    private fun getHttpClient(): HttpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
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
            pressureUnitAdapter = pressureUnitSqlAdapter
        )
    )

    private fun getMetNorwayForecastProvider(
        userAgent: String,
        dotDecimalFormatter: DotDecimalFormatter,
        rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser,
        metaQueries: MetaQueries,
        cachedResponseQueries: CachedResponseQueries,
        ioDispatcher: CoroutineDispatcher
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
        rfc1123ToEpochMillis = rfc1123UtcDateTimeParser::parseToEpochMillis
    )

    private fun getOsmPlaceSearcher(userAgent: String): OsmPlaceSearcher = OsmPlaceSearcher(
        osmPlaceService = OsmPlaceService(
            client = getHttpClient(),
            baseUrl = "https://nominatim.openstreetmap.org",
            userAgent = userAgent
        )
    )
}