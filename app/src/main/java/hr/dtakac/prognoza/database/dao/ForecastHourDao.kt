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

    @Query("DELETE FROM ForecastHour WHERE DATE(date) < DATE('now')")
    suspend fun deletePastForecastHours()

    @Query(
        """
            SELECT * FROM ForecastHour 
            WHERE DATETIME(date) BETWEEN DATETIME(:startDateTime) AND DATETIME(:startDateTime, '+' || :numberOfHours || ' hours') 
            AND locationId == :locationId 
            ORDER BY DATETIME(date) ASC
        """
    )
    suspend fun getForecastHours(
        startDateTime: String,
        numberOfHours: Int,
        locationId: Long
    ): List<ForecastHour>
}