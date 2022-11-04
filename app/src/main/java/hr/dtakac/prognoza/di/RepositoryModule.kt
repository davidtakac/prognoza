package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.MetNorwayDatabase
import hr.dtakac.prognoza.PrognozaDatabase
import hr.dtakac.prognoza.data.repository.ForecastRepository
import hr.dtakac.prognoza.data.repository.DefaultSettingsRepository
import hr.dtakac.prognoza.data.repository.PlaceRepository
import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.domain.forecast.ForecastProvider
import hr.dtakac.prognoza.domain.forecast.ForecastSaver
import hr.dtakac.prognoza.domain.forecast.SavedForecastGetter
import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.domain.place.PlaceSaver
import hr.dtakac.prognoza.domain.place.PlaceSearcher
import hr.dtakac.prognoza.metnorwayforecastprovider.MetNorwayForecastService
import hr.dtakac.prognoza.metnorwayforecastprovider.MetNorwayForecastProvider
import hr.dtakac.prognoza.osmplacesearcher.OsmPlaceSearcher
import hr.dtakac.prognoza.osmplacesearcher.OsmPlaceService
import kotlinx.coroutines.CoroutineDispatcher
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
        database: PrognozaDatabase,
        savedPlaceGetter: SavedPlaceGetter,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): SettingsRepository = DefaultSettingsRepository(
        settingsQueries = database.settingsQueries,
        savedPlaceGetter = savedPlaceGetter,
        ioDispatcher = ioDispatcher
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
        metNorwayForecastService: MetNorwayForecastService,
        metNorwayDatabase: MetNorwayDatabase,
        @Named("io")
        ioDispatcher: CoroutineDispatcher
    ): ForecastProvider = MetNorwayForecastProvider(
        apiService = metNorwayForecastService,
        database = metNorwayDatabase,
        ioDispatcher = ioDispatcher
    )

    @Provides
    fun provideForecastRepository(
        database: PrognozaDatabase,
        @Named("computation") computationDispatcher: CoroutineDispatcher,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): ForecastRepository = ForecastRepository(
        forecastQueries = database.forecastQueries,
        computationDispatcher = computationDispatcher,
        ioDispatcher = ioDispatcher
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
        osmPlaceService: OsmPlaceService
    ): PlaceSearcher = OsmPlaceSearcher(osmPlaceService)

    @Provides
    fun providePlaceRepository(
        database: PrognozaDatabase,
        @Named("io") ioDispatcher: CoroutineDispatcher,
        @Named("computation") computationDispatcher: CoroutineDispatcher
    ): PlaceRepository = PlaceRepository(
        placeQueries = database.placeQueries,
        ioDispatcher = ioDispatcher,
        computationDispatcher = computationDispatcher
    )
}