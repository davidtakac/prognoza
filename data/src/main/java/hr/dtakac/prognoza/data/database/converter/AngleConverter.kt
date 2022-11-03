package hr.dtakac.prognoza.data.database.converter

import hr.dtakac.prognoza.entities.forecast.units.Angle
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit

object AngleConverter {
    @JvmStatic
    fun fromDouble(value: Double?): Angle? = value?.let {
        Angle(it, AngleUnit.DEG)
    }

    @JvmStatic
    fun toDouble(angle: Angle?): Double? = angle?.degrees
}