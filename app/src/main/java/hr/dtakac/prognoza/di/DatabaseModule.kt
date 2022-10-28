package hr.dtakac.prognoza.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.metnorwayforecastprovider.database.MetNorwayDatabase
import hr.dtakac.prognoza.metnorwayforecastprovider.database.converter.ForecastResponseConverter
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePrognozaDatabase(
        @ApplicationContext context: Context
    ): PrognozaDatabase = Room.databaseBuilder(context, PrognozaDatabase::class.java, "prognoza_database").build()

    @Provides
    @Singleton
    fun provideMetNorwayDatabase(
        @ApplicationContext context: Context,
        json: Json
    ): MetNorwayDatabase = Room.databaseBuilder(context, MetNorwayDatabase::class.java, "met_norway_database")
        .addTypeConverter(ForecastResponseConverter(json))
        .build()
}