package hr.dtakac.prognoza.entities.forecast.units

import kotlin.math.PI
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit.*

class Angle(
    value: Double,
    unit: AngleUnit
) {
    val degrees: Double = if (unit == DEG) value else value * 180/PI
}

enum class AngleUnit {
    DEG, RAD
}