package hr.dtakac.prognoza.metnorwayforecastprovider.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import hr.dtakac.prognoza.metnorwayforecastprovider.LocationForecastResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class ForecastResponseConverter(private val json: Json) {
    @TypeConverter
    fun toString(response: LocationForecastResponse?): String? = response?.let {
        json.encodeToString(it)
    }

    @TypeConverter
    fun fromString(str: String?): LocationForecastResponse? = str?.let {
        json.decodeFromString(it)
    }
}