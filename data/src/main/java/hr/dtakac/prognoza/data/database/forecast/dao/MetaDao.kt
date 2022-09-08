package hr.dtakac.prognoza.data.database.forecast.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.data.database.forecast.model.MetaDbModel

@Dao
interface MetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(forecastMeta: MetaDbModel)

    @Query("SELECT * FROM meta WHERE latitude == :latitude AND longitude == :longitude")
    suspend fun get(latitude: Double, longitude: Double): MetaDbModel?
}