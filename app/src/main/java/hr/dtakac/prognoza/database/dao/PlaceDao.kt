package hr.dtakac.prognoza.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.database.entity.Place

@Dao
interface PlaceDao {
    @Query("SELECT * FROM Place WHERE id == :id")
    suspend fun get(id: String): Place?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(place: Place)
}