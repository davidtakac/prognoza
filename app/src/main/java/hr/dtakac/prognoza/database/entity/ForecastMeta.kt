package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastMeta(
    @PrimaryKey
    val placeId: String,
    /**
     * Timestamp when forecast data expires. In RFC1123 format.
     */
    val expires: String,
    /**
     * Timestamp] when forecast data was last modified. In RFC1123 format.
     */
    val lastModified: String
)