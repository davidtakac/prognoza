package hr.dtakac.prognoza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dtakac.prognoza.data.database.forecast.ForecastDao
import hr.dtakac.prognoza.data.database.forecast.ForecastDbModel
import hr.dtakac.prognoza.data.database.place.PlaceDao
import hr.dtakac.prognoza.data.database.place.PlaceDbModel

@Database(
    entities = [
        ForecastDbModel::class,
        PlaceDbModel::class,
    ],
    version = 2,
    exportSchema = true
)
abstract class PrognozaDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun placeDao(): PlaceDao
}