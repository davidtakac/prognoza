package hr.dtakac.prognoza

import android.content.Context
import androidx.room.Room
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.api.PlaceService
import hr.dtakac.prognoza.common.network.DefaultNetworkChecker
import hr.dtakac.prognoza.common.network.NetworkChecker
import hr.dtakac.prognoza.coroutines.DefaultDispatcherProvider
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.AppDatabase
import hr.dtakac.prognoza.repository.forecast.DefaultForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.meta.DefaultMetaRepository
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.place.DefaultPlaceRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.DefaultPreferencesRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.viewmodel.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val metNorwayRetrofit = named("MET Norway")
private val nominatimRetrofit = named("OSM Nominatim")

val prognozaAppModule = module {
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
            AppDatabase::class.java, "prognoza_database"
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

    factory<MetaRepository> {
        DefaultMetaRepository(get<AppDatabase>().metaDao())
    }

    factory<PlaceRepository> {
        DefaultPlaceRepository(get<AppDatabase>().placeDao(), get(), get())
    }

    factory<ForecastRepository> {
        DefaultForecastRepository(
            dispatcherProvider = get(),
            forecastService = get(),
            forecastDao = get<AppDatabase>().hourDao(),
            placeRepository = get(),
            metaRepository = get()
        )
    }

    factory<NetworkChecker> {
        DefaultNetworkChecker(androidContext())
    }

    viewModel {
        DaysFragmentViewModel(null, get(), get(), get())
    }

    viewModel {
        ForecastActivityViewModel(null, get(), get(), get())
    }

    viewModel {
        TodayFragmentViewModel(null, get(), get(), get())
    }

    viewModel {
        TomorrowFragmentViewModel(null, get(), get(), get())
    }

    viewModel {
        PlacesViewModel(null, get(), get(), get(), get())
    }
}