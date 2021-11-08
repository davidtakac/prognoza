package hr.dtakac.prognoza.core.di

import android.content.Context
import androidx.room.Room
import hr.dtakac.prognoza.core.api.ForecastService
import hr.dtakac.prognoza.core.api.PlaceService
import hr.dtakac.prognoza.core.coroutines.DefaultDispatcherProvider
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.database.PrognozaDatabase
import hr.dtakac.prognoza.core.network.DefaultNetworkChecker
import hr.dtakac.prognoza.core.network.NetworkChecker
import hr.dtakac.prognoza.core.repository.forecast.DefaultForecastRepository
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.repository.meta.DefaultMetaRepository
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.DefaultPlaceRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.DefaultPreferencesRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.timeprovider.DefaultForecastTimeProvider
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.core.utils.MET_NORWAY_BASE_URL
import hr.dtakac.prognoza.core.utils.OSM_NOMINATIM_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val metNorwayRetrofit = named("MET Norway")
private val nominatimRetrofit = named("OSM Nominatim")

val coreModule = module {
    single(metNorwayRetrofit) {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        Retrofit.Builder()
            .baseUrl(MET_NORWAY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    single(nominatimRetrofit) {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        Retrofit.Builder()
            .baseUrl(OSM_NOMINATIM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    single<ForecastService> {
        get<Retrofit>(metNorwayRetrofit).create(ForecastService::class.java)
    }

    single<PlaceService> {
        get<Retrofit>(nominatimRetrofit).create(PlaceService::class.java)
    }

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }

    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            PrognozaDatabase::class.java, "prognoza_database"
        ).build()
    }

    single<PreferencesRepository> {
        DefaultPreferencesRepository(
            androidApplication().getSharedPreferences(
                "shared_preferences",
                Context.MODE_PRIVATE
            ), get()
        )
    }

    single<MetaRepository> {
        DefaultMetaRepository(get<PrognozaDatabase>().metaDao())
    }

    single<PlaceRepository> {
        DefaultPlaceRepository(get<PrognozaDatabase>().placeDao(), get(), get())
    }

    single<ForecastRepository> {
        DefaultForecastRepository(
            dispatcherProvider = get(),
            forecastService = get(),
            forecastDao = get<PrognozaDatabase>().hourDao(),
            metaRepository = get()
        )
    }

    single<ForecastTimeProvider> {
        DefaultForecastTimeProvider()
    }

    factory<NetworkChecker> {
        DefaultNetworkChecker(androidContext())
    }
}