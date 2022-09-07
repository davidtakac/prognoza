package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Speed
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit

object SpeedConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Speed? = value?.let {
        Speed(it, SpeedUnit.MPS)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(speed: Speed?): Double? = speed?.metersPerSecond
}