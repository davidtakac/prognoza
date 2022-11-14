package hr.dtakac.prognoza.shared.data.metnorway

import com.squareup.sqldelight.ColumnAdapter
import hr.dtakac.prognoza.shared.data.metnorway.network.LocationForecastResponse
import kotlinx.serialization.json.Json

val responseSqlAdapter = object : ColumnAdapter<LocationForecastResponse, String> {
    override fun decode(databaseValue: String): LocationForecastResponse {
        return Json.decodeFromString(LocationForecastResponse.serializer(), databaseValue)
    }

    override fun encode(value: LocationForecastResponse): String {
        return Json.encodeToString(LocationForecastResponse.serializer(), value)
    }
}