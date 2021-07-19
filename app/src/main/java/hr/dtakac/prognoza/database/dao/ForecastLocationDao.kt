package hr.dtakac.prognoza.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.database.entity.ForecastLocation

@Dao
interface ForecastLocationDao {
    @Query("SELECT * FROM ForecastLocation WHERE id == :id")
    suspend fun get(id: Long): ForecastLocation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(forecastLocation: ForecastLocation)
}