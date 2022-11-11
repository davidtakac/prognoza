package hr.dtakac.prognoza.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.MetNorwayDatabase
import hr.dtakac.prognoza.PrognozaDatabase
import hr.dtakac.prognoza.data.*
import hr.dtakac.prognoza.metnorwayforecastprovider.CachedResponse
import hr.dtakac.prognoza.metnorwayforecastprovider.responseSqlAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePrognozaDatabase(
        @ApplicationContext context: Context
    ): PrognozaDatabase = PrognozaDatabase(
        driver = AndroidSqliteDriver(PrognozaDatabase.Schema, context, "prognoza.db"),
        ForecastAdapter = Forecast.Adapter(
            temperatureAdapter = temperatureSqlAdapter,
            descriptionAdapter = descriptionSqlAdapter,
            moodAdapter = moodSqlAdapter,
            precipitationAdapter = lengthSqlAdapter,
            windSpeedAdapter = speedSqlAdapter,
            windFromDirectionAdapter = angleSqlAdapter,
            humidityAdapter = percentageSqlAdapter,
            airPressureAtSeaLevelAdapter = pressureSqlAdapter
        ),
        SettingsAdapter = Settings.Adapter(
            temperatureUnitAdapter = temperatureUnitSqlAdapter,
            precipitationUnitAdapter = lengthUnitSqlAdapter,
            windUnitAdapter = speedUnitSqlAdapter,
            pressureUnitAdapter = pressureUnitSqlAdapter
        )
    )

    @Provides
    @Singleton
    fun provideMetNorwayDatabase(
        @ApplicationContext context: Context
    ): MetNorwayDatabase = MetNorwayDatabase(
        driver = AndroidSqliteDriver(MetNorwayDatabase.Schema, context, "met_norway.db"),
        CachedResponseAdapter = CachedResponse.Adapter(
            responseAdapter = responseSqlAdapter
        )
    )
}