package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit

object LengthConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Length? = value?.let {
        Length(it, unit = LengthUnit.MM)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(length: Length?): Double? = length?.millimeters
}