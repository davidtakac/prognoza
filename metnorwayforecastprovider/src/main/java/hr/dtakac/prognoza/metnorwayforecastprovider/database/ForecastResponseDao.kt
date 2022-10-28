package hr.dtakac.prognoza.metnorwayforecastprovider.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ForecastResponseDao {
    @Query("SELECT * FROM forecast_response WHERE abs(latitude - :latitude) < 0.00001 AND abs(longitude - :longitude) < 0.00001")
    suspend fun get(latitude: Double, longitude: Double): ForecastResponseDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ForecastResponseDbModel)
}