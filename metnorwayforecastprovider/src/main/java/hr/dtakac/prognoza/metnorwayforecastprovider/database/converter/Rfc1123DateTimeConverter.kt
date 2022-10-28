package hr.dtakac.prognoza.metnorwayforecastprovider.database.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Rfc1123DateTimeConverter {
    private val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

    @JvmStatic
    @TypeConverter
    fun fromTimestamp(timestamp: String?): ZonedDateTime? {
        return timestamp?.let {
            try {
                ZonedDateTime.parse(timestamp, formatter)
            } catch (e: Exception) {
                null
            }
        }
    }

    @JvmStatic
    @TypeConverter
    fun toTimestamp(dateTime: ZonedDateTime?): String? {
        return try {
            dateTime?.format(formatter)
        } catch (e: Exception) {
            null
        }
    }
}