package hr.dtakac.prognoza.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["dateTime", "locationId"])
data class ForecastHour(
    val dateTime: String,
    val locationId: Long,
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
)