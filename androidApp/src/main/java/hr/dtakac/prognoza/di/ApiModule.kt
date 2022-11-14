package hr.dtakac.prognoza.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.platform.AndroidDotDecimalFormatter
import hr.dtakac.prognoza.shared.data.metnorway.network.MetNorwayForecastService
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceService
import hr.dtakac.prognoza.shared.platform.Rfc1123UtcDateTimeParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        json: Json
    ): HttpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(json)
        }
    }

    @Provides
    @Singleton
    fun provideForecastService(
        httpClient: HttpClient,
        @Named("user_agent")
        userAgent: String,
        rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser
    ): MetNorwayForecastService {
        return MetNorwayForecastService(
            client = httpClient,
            userAgent = userAgent,
            baseUrl = "https://api.met.no/weatherapi",
            dotDecimalFormatter = AndroidDotDecimalFormatter(),
            epochMillisToRfc1123 = rfc1123UtcDateTimeParser::format
        )
    }

    @Provides
    @Singleton
    fun providePlaceService(
        httpClient: HttpClient,
        @Named("user_agent")
        userAgent: String
    ): OsmPlaceService {
        return OsmPlaceService(
            client = httpClient,
            userAgent = userAgent,
            baseUrl = "https://nominatim.openstreetmap.org"
        )
    }
}