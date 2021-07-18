package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastLocation(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val name: String,
    val postCode: String,
    val country: String
)