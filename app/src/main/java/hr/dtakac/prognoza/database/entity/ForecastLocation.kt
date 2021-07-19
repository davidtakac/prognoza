package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val postCode: String,
    val country: String,
    val latitude: Float,
    val longitude: Float
)