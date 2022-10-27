package hr.dtakac.prognoza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.data.database.forecast.dao.ForecastDao
import hr.dtakac.prognoza.data.database.forecast.dao.MetaDao
import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import hr.dtakac.prognoza.data.database.forecast.model.MetaDbModel
import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.database.place.PlaceDbModel

@Database(
    entities = [
        ForecastDbModel::class,
        PlaceDbModel::class,
        MetaDbModel::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PrognozaDatabase : RoomDatabase() {
    abstract fun metaDao(): MetaDao
    abstract fun forecastDao(): ForecastDao
    abstract fun placeDao(): PlaceDao
}