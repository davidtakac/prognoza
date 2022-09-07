package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

object TemperatureConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Temperature? = value?.let {
        Temperature(it, unit = TemperatureUnit.C)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(temperature: Temperature?): Double? = temperature?.celsius
}