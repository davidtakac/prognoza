package hr.dtakac.prognoza.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.data.network.place.PlaceService
import hr.dtakac.prognoza.data.repository.DefaultForecastRepository
import hr.dtakac.prognoza.data.repository.DefaultPlaceRepository
import hr.dtakac.prognoza.data.repository.DefaultSettingsRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.themesetting.repository.ThemeSettingRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("prognoza_prefs", Context.MODE_PRIVATE)

    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        sharedPreferences: SharedPreferences,
        database: PrognozaDatabase
    ): SettingsRepository = DefaultSettingsRepository(
        sharedPreferences,
        database.placeDao(),
        ioDispatcher = Dispatchers.Default
    )

    @Provides
    @ViewModelScoped
    fun provideForecastRepository(
        forecastService: ForecastService,
        database: PrognozaDatabase,
        @Named("user_agent") userAgent: String
    ): ForecastRepository = DefaultForecastRepository(
        forecastService,
        database.forecastDao(),
        database.metaDao(),
        userAgent = userAgent,
        computationDispatcher = Dispatchers.Default
    )

    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
    fun provideThemeSettingRepository(
        sharedPreferences: SharedPreferences
    ): ThemeSettingRepository = ThemeSettingRepository(sharedPreferences)
}