package hr.dtakac.prognoza.core.database.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ForecastTimeSpanDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val zoneId = ZoneId.of("Etc/GMT")

    @JvmStatic
    @TypeConverter
    fun fromTimestamp(timestamp: String?): ZonedDateTime? {
        return timestamp?.let {
            try {
                LocalDateTime.parse(timestamp, formatter).atZone(zoneId)
            } catch (e: Exception) {
                null
            }
        }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(dateTime: ZonedDateTime?): String? {
        return try {
            dateTime
                ?.withZoneSameInstant(zoneId)
                ?.toLocalDateTime()
                ?.format(formatter)
        } catch (e: Exception) {
            null
        }
    }
}