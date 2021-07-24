package hr.dtakac.prognoza

import android.content.Context
import androidx.room.Room
import hr.dtakac.prognoza.api.ForecastService
import hr.dtakac.prognoza.coroutines.DefaultDispatcherProvider
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.AppDatabase
import hr.dtakac.prognoza.forecast.viewmodel.ForecastViewModel
import hr.dtakac.prognoza.repository.forecast.DefaultForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.location.DefaultLocationRepository
import hr.dtakac.prognoza.repository.location.LocationRepository
import hr.dtakac.prognoza.repository.meta.DefaultMetaRepository
import hr.dtakac.prognoza.repository.meta.MetaRepository
import hr.dtakac.prognoza.repository.preferences.DefaultPreferencesRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val prognozaAppModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    single<ForecastService> {
        get<Retrofit>().create(ForecastService::class.java)
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
        DefaultPreferencesRepository(androidApplication().getSharedPreferences(
            "shared_preferences",
            Context.MODE_PRIVATE
        ))
    }

    factory<MetaRepository> {
        DefaultMetaRepository(get<AppDatabase>().metaDao())
    }

    factory<LocationRepository> {
        DefaultLocationRepository(get<AppDatabase>().locationDao(), get())
    }

    factory<ForecastRepository> {
        DefaultForecastRepository(
            dispatcherProvider = get(),
            forecastService = get(),
            forecastDao = get<AppDatabase>().hourDao(),
            locationRepository = get(),
            metaRepository = get(),
            preferencesRepository = get()
        )
    }

    viewModel {
        ForecastViewModel(null, get(), get(), get())
    }
}