package hr.dtakac.prognoza.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.entity.ForecastMeta

@Dao
interface ForecastMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateForecastMeta(forecastMeta: ForecastMeta)

    @Query("SELECT * FROM ForecastMeta WHERE placeId = :placeId")
    suspend fun get(placeId: String): ForecastMeta?
}