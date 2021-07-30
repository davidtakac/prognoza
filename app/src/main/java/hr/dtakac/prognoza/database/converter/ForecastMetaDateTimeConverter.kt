package hr.dtakac.prognoza.database.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ForecastMetaDateTimeConverter {
    private val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

    @JvmStatic
    @TypeConverter
    fun fromTimestamp(timestamp: String?): ZonedDateTime? {
        return timestamp?.let {
            ZonedDateTime.parse(timestamp, formatter)
        }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(dateTime: ZonedDateTime?): String? {
        return dateTime?.format(formatter)
    }
}