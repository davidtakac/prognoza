package hr.dtakac.prognoza.data.database.forecast.dao

import androidx.room.*
import hr.dtakac.prognoza.data.database.converter.ForecastDateTimeConverter
import hr.dtakac.prognoza.data.database.forecast.model.ForecastDbModel
import java.time.ZonedDateTime

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(forecastTimeSpans: List<ForecastDbModel>)

    @Query("DELETE FROM forecast WHERE DATE(start_time) < DATE('now')")
    suspend fun deleteExpiredForecastTimeSpans()

    @Query(
        value = """
            SELECT * FROM forecast 
            WHERE DATETIME(start_time) BETWEEN DATETIME(:start) AND DATETIME(:end) 
            AND latitude == :latitude AND longitude == :longitude 
            ORDER BY DATETIME(start_time) ASC
        """
    )
    @TypeConverters(ForecastDateTimeConverter::class)
    suspend fun getForecasts(
        start: ZonedDateTime,
        end: ZonedDateTime,
        latitude: Double,
        longitude: Double
    ): List<ForecastDbModel>
}