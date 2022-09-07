package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Pressure
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit

object PressureConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Pressure? = value?.let {
        Pressure(it, PressureUnit.HPA)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(pressure: Pressure?): Double? = pressure?.hectoPascal
}