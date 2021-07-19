package hr.dtakac.prognoza.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.database.entity.FORECAST_META_KEY
import hr.dtakac.prognoza.database.entity.ForecastMeta

@Dao
interface ForecastMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateForecastMeta(forecastMeta: ForecastMeta)

    @Query("SELECT * FROM ForecastMeta WHERE id = $FORECAST_META_KEY")
    suspend fun get(): ForecastMeta?
}