package hr.dtakac.prognoza.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dtakac.prognoza.database.entity.ForecastHour

@Dao
interface ForecastHourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(forecastHours: List<ForecastHour>)

    @Query("DELETE FROM ForecastHour WHERE DATE(dateTime) < DATE('now')")
    suspend fun deletePastForecastHours()

    @Query(
        value = """
            SELECT * FROM ForecastHour 
            WHERE DATETIME(dateTime) BETWEEN DATETIME(:startDateTime) AND DATETIME(:endDateTime) 
            AND locationId == :locationId 
            ORDER BY DATETIME(dateTime) ASC
        """
    )
    suspend fun getForecastHours(startDateTime: String, endDateTime: String, locationId: Long): List<ForecastHour>
}