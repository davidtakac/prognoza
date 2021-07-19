package hr.dtakac.prognoza.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["date", "locationId"])
data class ForecastHour(
    val date: String,
    val locationId: Long,
    val temperature: Float?,
    val symbolCode: String?,
    val precipitationProbability: Float?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
)