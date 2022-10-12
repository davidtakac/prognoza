package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.data.network.place.PlaceService
import hr.dtakac.prognoza.data.repository.DefaultForecastRepository
import hr.dtakac.prognoza.data.repository.DefaultPlaceRepository
import hr.dtakac.prognoza.data.repository.DefaultSettingsRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.ui.ThemeChanger
import hr.dtakac.prognoza.ui.WidgetRefresher
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
        sharedPreferences: SharedPreferences,
        database: PrognozaDatabase,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): SettingsRepository = DefaultSettingsRepository(
        sharedPreferences,
        database.placeDao(),
        ioDispatcher
    )

    @Provides
    fun provideForecastRepository(
        forecastService: ForecastService,
        database: PrognozaDatabase,
        @Named("user_agent") userAgent: String,
        @Named("computation") computationDispatcher: CoroutineDispatcher
    ): ForecastRepository = DefaultForecastRepository(
        forecastService,
        database.forecastDao(),
        database.metaDao(),
        userAgent,
        computationDispatcher
    )

    @Provides
    fun providePlaceRepository(
        placeService: PlaceService,
        database: PrognozaDatabase,
        @Named("user_agent") userAgent: String
    ): PlaceRepository = DefaultPlaceRepository(
        placeService,
        placeDao = database.placeDao(),
        userAgent = userAgent
    )

    @Provides
    fun provideThemeChanger(
        sharedPreferences: SharedPreferences,
        @Named("io") ioDispatcher: CoroutineDispatcher
    ): ThemeChanger = ThemeChanger(sharedPreferences, ioDispatcher)

    @Provides
    fun provideWidgetRefresher(
        @ApplicationContext context: Context
    ): WidgetRefresher = WidgetRefresher(context)
}