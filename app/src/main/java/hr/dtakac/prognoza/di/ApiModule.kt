package hr.dtakac.prognoza.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.metnorwayforecastprovider.ForecastService
import hr.dtakac.prognoza.osmplacesearcher.PlaceService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.time.Duration
import javax.inject.Named
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
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
    fun provideJsonConverterFactory(
        json: Json
    ): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val timeout = Duration.ofSeconds(10L)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(timeout)
            .readTimeout(timeout)
            .writeTimeout(timeout)
            .build()
    }

    @Provides
    @Singleton
    fun provideForecastService(
        okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): ForecastService {
        return Retrofit.Builder()
            .baseUrl("https://api.met.no/weatherapi/")
            .addConverterFactory(jsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(ForecastService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaceService(
        httpClient: HttpClient,
        @Named("user_agent")
        userAgent: String
    ): PlaceService {
        return PlaceService(
            client = httpClient,
            userAgent = userAgent,
            baseUrl = "https://nominatim.openstreetmap.org/"
        )
    }
}