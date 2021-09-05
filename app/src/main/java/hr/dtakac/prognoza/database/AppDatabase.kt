package hr.dtakac.prognoza.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.dao.ForecastTimeSpanDao
import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.dbmodel.ForecastMeta
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.dbmodel.Place

@Database(
    entities = [
        ForecastTimeSpan::class,
        Place::class,
        ForecastMeta::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metaDao(): ForecastMetaDao
    abstract fun hourDao(): ForecastTimeSpanDao
    abstract fun placeDao(): PlaceDao
}