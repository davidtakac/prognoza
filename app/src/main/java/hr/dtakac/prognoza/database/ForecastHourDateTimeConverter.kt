package hr.dtakac.prognoza.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ForecastHourDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val zoneId = ZoneId.of("Etc/GMT")

    @JvmStatic
    @TypeConverter
    fun fromTimestamp(timestamp: String?): ZonedDateTime? {
        return timestamp?.let {
            LocalDateTime
                .parse(timestamp, formatter)
                .atZone(zoneId)
        }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(dateTime: ZonedDateTime?): String? {
        return dateTime
            ?.withZoneSameInstant(zoneId)
            ?.toLocalDateTime()
            ?.format(formatter)
    }
}