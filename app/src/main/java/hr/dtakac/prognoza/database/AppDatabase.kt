package hr.dtakac.prognoza.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.database.dao.ForecastHourDao
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.dao.PlaceDao
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.database.entity.Place

@Database(
    entities = [
        ForecastHour::class,
        Place::class,
        ForecastMeta::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metaDao(): ForecastMetaDao
    abstract fun hourDao(): ForecastHourDao
    abstract fun placeDao(): PlaceDao
}