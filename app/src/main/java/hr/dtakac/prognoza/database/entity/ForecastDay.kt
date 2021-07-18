package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastDay(
    @PrimaryKey
    val key: Int = 0,
    val date: String,
    val symbolCode: String,
    val lowTemperature: String,
    val highTemperature: String
)