package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastHour(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val date: String,
    val lowTemperature: Float,
    val highTemperature: Float,
    val symbolCode: String,
    val precipitationProbability: Float,
    val precipitationAmount: Float,
    val windSpeed: Float,
    val locationKey: String
)