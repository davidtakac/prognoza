package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ForecastMetaDao {
    @Query("SELECT * FROM forecast_meta WHERE abs(latitude - :latitude) < 0.00001 AND abs(longitude - :longitude) < 0.00001")
    suspend fun get(latitude: Double, longitude: Double): ForecastMetaDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ForecastMetaDbModel)
}