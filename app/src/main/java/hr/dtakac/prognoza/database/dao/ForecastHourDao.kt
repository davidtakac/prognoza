package hr.dtakac.prognoza.database.dao

import androidx.room.*
import hr.dtakac.prognoza.database.converter.ForecastTimeSpanDateTimeConverter
import hr.dtakac.prognoza.entity.ForecastTimeSpan
import java.time.ZonedDateTime

@Dao
interface ForecastTimeSpanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(forecastTimeSpans: List<ForecastTimeSpan>)

    @Query("DELETE FROM ForecastTimeSpan WHERE DATE(startTime) < DATE('now')")
    suspend fun deleteExpiredForecastTimeSpans()

    @Query(
        value = """
            SELECT * FROM ForecastTimeSpan 
            WHERE DATETIME(startTime) BETWEEN DATETIME(:start) AND DATETIME(:end) 
            AND placeId == :placeId 
            ORDER BY DATETIME(startTime) ASC
        """
    )
    @TypeConverters(ForecastTimeSpanDateTimeConverter::class)
    suspend fun getForecastTimeSpans(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): List<ForecastTimeSpan>
}