package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Angle
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit

object AngleConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Angle? = value?.let {
        Angle(it, AngleUnit.DEG)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(angle: Angle?): Double? = angle?.degrees
}