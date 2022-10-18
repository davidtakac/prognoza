package hr.dtakac.prognoza.data.database.forecast.dao

import androidx.room.*
import hr.dtakac.prognoza.data.database.converter.IsoLocalDateTimeConverter
import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import java.time.ZonedDateTime

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbModels: List<ForecastDbModel>)

    @Query("DELETE FROM forecast WHERE abs(latitude - :latitude) < 0.00001 AND abs(longitude - :longitude) < 0.00001")
    suspend fun delete(latitude: Double, longitude: Double)

    @Query(
        value = """
            SELECT * FROM forecast 
            WHERE DATETIME(start_time) BETWEEN DATETIME(:start) AND DATETIME(:end) 
            AND abs(latitude - :latitude) < 0.00001 AND abs(longitude - :longitude) < 0.00001
            ORDER BY DATETIME(start_time) ASC
        """
    )
    @TypeConverters(IsoLocalDateTimeConverter::class)
    suspend fun getForecasts(
        start: ZonedDateTime,
        end: ZonedDateTime,
        latitude: Double,
        longitude: Double
    ): List<ForecastDbModel>
}