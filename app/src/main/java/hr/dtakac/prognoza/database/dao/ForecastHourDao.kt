package hr.dtakac.prognoza.database.dao

import androidx.room.*
import hr.dtakac.prognoza.database.converter.ForecastHourDateTimeConverter
import hr.dtakac.prognoza.dbmodel.ForecastHour
import java.time.ZonedDateTime

@Dao
interface ForecastHourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(forecastHours: List<ForecastHour>)

    @Query("DELETE FROM ForecastHour WHERE DATE(time) < DATE('now')")
    suspend fun deleteExpiredForecastHours()

    @Query(
        value = """
            SELECT * FROM ForecastHour 
            WHERE DATETIME(time) BETWEEN DATETIME(:start) AND DATETIME(:end) 
            AND placeId == :placeId 
            ORDER BY DATETIME(time) ASC
        """
    )
    @TypeConverters(ForecastHourDateTimeConverter::class)
    suspend fun getForecastHours(
        start: ZonedDateTime,
        end: ZonedDateTime,
        placeId: String
    ): List<ForecastHour>
}