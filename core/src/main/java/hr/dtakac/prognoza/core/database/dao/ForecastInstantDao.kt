package hr.dtakac.prognoza.core.database.dao

import androidx.room.*
import hr.dtakac.prognoza.core.database.converter.ForecastInstantDateTimeConverter
import hr.dtakac.prognoza.core.model.database.ForecastInstant
import java.time.ZonedDateTime

@Dao
interface ForecastInstantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(forecastInstants: List<ForecastInstant>)

    @Query("DELETE FROM ForecastInstant WHERE DATE(time) < DATE('now')")
    suspend fun deletePastForecastInstants()

    @Query(
        value = """
            SELECT * FROM ForecastInstant 
            WHERE DATETIME(time) BETWEEN DATETIME(:start) AND DATETIME(:end) 
            AND placeId == :placeId 
            ORDER BY DATETIME(time) ASC
        """
    )
    @TypeConverters(ForecastInstantDateTimeConverter::class)
    suspend fun getForecastInstants(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): List<ForecastInstant>
}