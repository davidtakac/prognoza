package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FORECAST_META_KEY = 1

@Entity
data class ForecastMeta(
    @PrimaryKey(autoGenerate = false)
    val key: Int = FORECAST_META_KEY,
    val expires: String,
    val lastModified: String
)