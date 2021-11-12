package hr.dtakac.prognoza.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.core.database.dao.ForecastMetaDao
import hr.dtakac.prognoza.core.database.dao.ForecastInstantDao
import hr.dtakac.prognoza.core.database.dao.PlaceDao
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastInstant
import hr.dtakac.prognoza.core.model.database.Place

@Database(
    entities = [
        ForecastInstant::class,
        Place::class,
        ForecastMeta::class
    ],
    version = 1
)
abstract class PrognozaDatabase : RoomDatabase() {
    abstract fun metaDao(): ForecastMetaDao
    abstract fun hourDao(): ForecastInstantDao
    abstract fun placeDao(): PlaceDao
}