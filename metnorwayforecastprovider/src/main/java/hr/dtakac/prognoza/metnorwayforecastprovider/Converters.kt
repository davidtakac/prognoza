package hr.dtakac.prognoza.metnorwayforecastprovider

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json

val responseSqlAdapter = object : ColumnAdapter<LocationForecastResponse, String> {
    override fun decode(databaseValue: String): LocationForecastResponse {
        return Json.decodeFromString(LocationForecastResponse.serializer(), databaseValue)
    }

    override fun encode(value: LocationForecastResponse): String {
        return Json.encodeToString(LocationForecastResponse.serializer(), value)
    }
}