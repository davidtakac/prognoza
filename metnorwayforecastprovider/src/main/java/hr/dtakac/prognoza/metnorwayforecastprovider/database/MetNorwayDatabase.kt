package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ForecastResponseDbModel::class,
        ForecastMetaDbModel::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class MetNorwayDatabase : RoomDatabase() {
    abstract fun metaDao(): ForecastMetaDao
    abstract fun forecastResponseDao(): ForecastResponseDao
}