package hr.dtakac.prognoza.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.data.network.forecast.ForecastService
import hr.dtakac.prognoza.data.repository.DefaultForecastRepository
import hr.dtakac.prognoza.data.repository.DefaultSettingsRepository
import hr.dtakac.prognoza.domain.repository.ForecastRepository
import hr.dtakac.prognoza.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        @ApplicationContext context: Context,
        database: PrognozaDatabase
    ): SettingsRepository = DefaultSettingsRepository(
        context.getSharedPreferences("prognoza_prefs", Context.MODE_PRIVATE),
        database.placeDao()
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
        defaultDispatcher = Dispatchers.Default
    )
}