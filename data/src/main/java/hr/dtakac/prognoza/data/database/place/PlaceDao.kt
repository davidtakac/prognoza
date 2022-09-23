package hr.dtakac.prognoza.data.database.place

import androidx.room.*

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place WHERE abs(latitude - :latitude) < 0.00001 AND abs(longitude - :longitude) < 0.00001")
    suspend fun get(
        latitude: Double,
        longitude: Double
    ): PlaceDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlaceDbModel)

    @Query("SELECT * FROM place ORDER BY name ASC")
    suspend fun getPlaces(): List<PlaceDbModel>
}