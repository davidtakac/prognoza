package hr.dtakac.prognoza.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.database.entity.ForecastDay
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.database.entity.ForecastLocation
import hr.dtakac.prognoza.database.entity.ForecastMeta

@Database(
    entities = [
        ForecastDay::class,
        ForecastHour::class,
        ForecastLocation::class,
        ForecastMeta::class
    ],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun metaDao() : ForecastMetaDao
}