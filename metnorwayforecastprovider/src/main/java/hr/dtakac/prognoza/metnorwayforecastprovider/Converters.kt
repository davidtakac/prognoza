package hr.dtakac.prognoza.metnorwayforecastprovider

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val zonedDateTimeSqlAdapter = object : ColumnAdapter<ZonedDateTime, String> {
    override fun decode(databaseValue: String): ZonedDateTime {
        return ZonedDateTime.parse(databaseValue, DateTimeFormatter.RFC_1123_DATE_TIME)
    }

    override fun encode(value: ZonedDateTime): String {
        return value.format(DateTimeFormatter.RFC_1123_DATE_TIME)
    }
}

val responseSqlAdapter = object : ColumnAdapter<LocationForecastResponse, String> {
    override fun decode(databaseValue: String): LocationForecastResponse {
        return Json.decodeFromString(LocationForecastResponse.serializer(), databaseValue)
    }

    override fun encode(value: LocationForecastResponse): String {
        return Json.encodeToString(LocationForecastResponse.serializer(), value)
    }
}