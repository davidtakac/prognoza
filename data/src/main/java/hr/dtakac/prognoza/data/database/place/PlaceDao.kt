package hr.dtakac.prognoza.data.database.place

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place WHERE latitude == :latitude AND longitude == :longitude")
    suspend fun get(latitude: Double, longitude: Double): PlaceDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlaceDbModel)

    @Query("SELECT * FROM place ORDER BY name ASC")
    suspend fun getPlaces(): List<PlaceDbModel>
}