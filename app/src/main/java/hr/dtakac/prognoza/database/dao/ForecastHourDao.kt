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
            WHERE DATETIME(dateTime) BETWEEN DATETIME(:startDateTime, '-1 hours') AND DATETIME(:startDateTime, '+1 days', 'start of day', '+5 hours') 
            AND locationId == :locationId 
            ORDER BY DATETIME(dateTime) ASC
        """
    )
    suspend fun getForecastHours(startDateTime: String, locationId: Long): List<ForecastHour>
}