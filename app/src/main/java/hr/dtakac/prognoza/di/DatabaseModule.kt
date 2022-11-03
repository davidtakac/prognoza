package hr.dtakac.prognoza.di

import android.content.Context
import androidx.room.Room
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.dtakac.prognoza.MetNorwayDatabase
import hr.dtakac.prognoza.data.database.PrognozaDatabase
import hr.dtakac.prognoza.metnorwayforecastprovider.CachedResponse
import hr.dtakac.prognoza.metnorwayforecastprovider.Meta
import hr.dtakac.prognoza.metnorwayforecastprovider.responseSqlAdapter
import hr.dtakac.prognoza.metnorwayforecastprovider.zonedDateTimeSqlAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePrognozaDatabase(
        @ApplicationContext context: Context
    ): PrognozaDatabase = Room.databaseBuilder(context, PrognozaDatabase::class.java, "prognoza_database")
        .fallbackToDestructiveMigrationFrom(1)
        .build()

    @Provides
    @Singleton
    fun provideMetNorwayDatabase(
        @ApplicationContext context: Context
    ): MetNorwayDatabase = MetNorwayDatabase(
        driver = AndroidSqliteDriver(MetNorwayDatabase.Schema, context, "met_norway.db"),
        CachedResponseAdapter = CachedResponse.Adapter(
            responseAdapter = responseSqlAdapter
        ),
        MetaAdapter = Meta.Adapter(
            expiresAdapter = zonedDateTimeSqlAdapter,
            lastModifiedAdapter = zonedDateTimeSqlAdapter
        )
    )
}