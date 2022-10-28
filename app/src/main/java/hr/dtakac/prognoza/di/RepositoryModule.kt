package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.data.repository.ForecastRepository
import hr.dtakac.prognoza.data.repository.DefaultSettingsRepository
import hr.dtakac.prognoza.data.repository.PlaceRepository
import hr.dtakac.prognoza.domain.forecast.ForecastProvider
import hr.dtakac.prognoza.domain.forecast.ForecastSaver
import hr.dtakac.prognoza.domain.forecast.SavedForecastGetter
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.place.PlaceSaver
import hr.dtakac.prognoza.domain.place.PlaceSearcher
import hr.dtakac.prognoza.domain.repository.*
import hr.dtakac.prognoza.metnorwayforecastprovider.ForecastService
import hr.dtakac.prognoza.metnorwayforecastprovider.MetNorwayForecastProvider
import hr.dtakac.prognoza.metnorwayforecastprovider.database.MetNorwayDatabase
import hr.dtakac.prognoza.osmplacesearcher.OsmPlaceSearcher
import hr.dtakac.prognoza.osmplacesearcher.PlaceService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("prognoza_prefs", Context.MODE_PRIVATE)

    @Provides
    fun provideSettingsRepository(
        sharedPreferences: SharedPreferences,
        savedPlaceGetter: SavedPlaceGetter,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): SettingsRepository = DefaultSettingsRepository(
        sharedPreferences,
        savedPlaceGetter,
        ioDispatcher
    )

    @Provides
    fun provideForecastSaver(
        forecastRepository: ForecastRepository
    ): ForecastSaver = forecastRepository

    @Provides
    fun provideSavedForecastGetter(
        forecastRepository: ForecastRepository
    ): SavedForecastGetter = forecastRepository

    @Provides
    fun provideForecastProvider(
        @Named("user_agent")
        userAgent: String,
        forecastService: ForecastService,
        metNorwayDatabase: MetNorwayDatabase,
    ): ForecastProvider = MetNorwayForecastProvider(
        userAgent = userAgent,
        forecastService = forecastService,
        metaDao = metNorwayDatabase.metaDao(),
        forecastResponseDao = metNorwayDatabase.forecastResponseDao()
    )

    @Provides
    fun provideForecastRepository(
        database: PrognozaDatabase,
        @Named("computation") computationDispatcher: CoroutineDispatcher
    ): ForecastRepository = ForecastRepository(
        forecastDao = database.forecastDao(),
        computationDispatcher = computationDispatcher
    )

    @Provides
    fun providePlaceSaver(
        placeRepository: PlaceRepository
    ): PlaceSaver = placeRepository

    @Provides
    fun providePlaceGetter(
        placeRepository: PlaceRepository
    ): SavedPlaceGetter = placeRepository

    @Provides
    fun providePlaceSearcher(
        placeService: PlaceService,
        @Named("user_agent")
        userAgent: String
    ): PlaceSearcher = OsmPlaceSearcher(userAgent, placeService)

    @Provides
    fun providePlaceRepository(
        database: PrognozaDatabase,
    ): PlaceRepository = PlaceRepository(database.placeDao())
}