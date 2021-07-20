package hr.dtakac.prognoza.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["timestamp", "locationId"])
data class ForecastHour(
    /**
     * Timestamp of the hour in ISO-8601 GMT.
     */
    val timestamp: String,
    val locationId: Long,
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
)