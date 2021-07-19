package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastDay(
    @PrimaryKey
    val id: Long,
    val date: String,
    val symbolCode: String,
    val lowTemperature: String,
    val highTemperature: String
)