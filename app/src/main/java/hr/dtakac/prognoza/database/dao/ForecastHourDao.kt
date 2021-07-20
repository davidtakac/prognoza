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

    @Query("DELETE FROM ForecastHour WHERE DATE(timestamp) < DATE('now')")
    suspend fun deletePastForecastHours()

    @Query(
        value = """
            SELECT * FROM ForecastHour 
            WHERE DATETIME(timestamp) BETWEEN DATETIME(:startDateTimeGmt) AND DATETIME(:endDateTimeGmt) 
            AND locationId == :locationId 
            ORDER BY DATETIME(timestamp) ASC
        """
    )
    suspend fun getForecastHours(startDateTimeGmt: String, endDateTimeGmt: String, locationId: Long): List<ForecastHour>
}