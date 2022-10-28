package hr.dtakac.prognoza.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.osmplacesprovider.PlaceService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.time.Duration
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    // The json converter factory is a singleton, so the Json instance gets created only once
    @Suppress("JSON_FORMAT_REDUNDANT")
    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.asConverterFactory("application/json".toMediaType())

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
        okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): PlaceService {
        return Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(jsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(PlaceService::class.java)
    }
}