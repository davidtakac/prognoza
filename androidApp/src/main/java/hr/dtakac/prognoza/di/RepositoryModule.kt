package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.shared.data.PrognozaDatabase
import hr.dtakac.prognoza.shared.data.metnorway.MetNorwayForecastProvider
import hr.dtakac.prognoza.shared.data.metnorway.network.MetNorwayForecastService
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceSearcher
import hr.dtakac.prognoza.shared.data.openstreetmap.OsmPlaceService
import hr.dtakac.prognoza.shared.data.prognoza.DatabaseForecastRepository
import hr.dtakac.prognoza.shared.data.prognoza.DatabasePlaceRepository
import hr.dtakac.prognoza.shared.data.prognoza.DatabaseSettingsRepository
import hr.dtakac.prognoza.shared.domain.data.*
import hr.dtakac.prognoza.shared.platform.Rfc1123UtcDateTimeParser
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
    ): SettingsRepository = DatabaseSettingsRepository(
        settingsQueries = database.settingsQueries,
        savedPlaceGetter = savedPlaceGetter,
        ioDispatcher = ioDispatcher
    )

    @Provides
    fun provideForecastSaver(
        forecastRepository: DatabaseForecastRepository
    ): ForecastSaver = forecastRepository

    @Provides
    fun provideSavedForecastGetter(
        forecastRepository: DatabaseForecastRepository
    ): SavedForecastGetter = forecastRepository

    @Provides
    fun provideForecastProvider(
        apiService: MetNorwayForecastService,
        database: PrognozaDatabase,
        @Named("io") ioDispatcher: CoroutineDispatcher,
        rfc1123UtcDateTimeParser: Rfc1123UtcDateTimeParser
    ): ForecastProvider = MetNorwayForecastProvider(
        apiService = apiService,
        ioDispatcher = ioDispatcher,
        metaQueries = database.metaQueries,
        cachedResponseQueries = database.cachedResponseQueries,
        rfc1123ToEpochMillis = rfc1123UtcDateTimeParser::parseToEpochMillis
    )

    @Provides
    fun provideForecastRepository(
        database: PrognozaDatabase,
        @Named("computation") computationDispatcher: CoroutineDispatcher,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): DatabaseForecastRepository = DatabaseForecastRepository(
        forecastQueries = database.forecastQueries,
        computationDispatcher = computationDispatcher,
        ioDispatcher = ioDispatcher
    )

    @Provides
    fun providePlaceSaver(
        placeRepository: DatabasePlaceRepository
    ): PlaceSaver = placeRepository

    @Provides
    fun providePlaceGetter(
        placeRepository: DatabasePlaceRepository
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
    ): DatabasePlaceRepository = DatabasePlaceRepository(
        placeQueries = database.placeQueries,
        ioDispatcher = ioDispatcher,
        computationDispatcher = computationDispatcher
    )
}