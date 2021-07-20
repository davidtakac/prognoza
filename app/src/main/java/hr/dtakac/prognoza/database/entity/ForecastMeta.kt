package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FORECAST_META_KEY = 1

@Entity
data class ForecastMeta(
    @PrimaryKey(autoGenerate = false)
    val id: Int = FORECAST_META_KEY,
    /**
     * Timestamp when forecast data expires. In RFC1123 format.
     */
    val expires: String,
    /**
     * Timestamp] when forecast data was last modified. In RFC1123 format.
     */
    val lastModified: String
)