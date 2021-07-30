package hr.dtakac.prognoza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

fun ForecastMeta?.hasExpired(): Boolean =
    this == null || ZonedDateTime.parse(
        expires,
        DateTimeFormatter.RFC_1123_DATE_TIME
    ) <= ZonedDateTime.now()