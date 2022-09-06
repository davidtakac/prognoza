package hr.dtakac.prognoza.entities.forecast.units

import kotlin.math.PI
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit.*

class Angle(
    private val value: Double,
    private val unit: AngleUnit
) {
    val degrees: Double = if (unit == DEG) value else value * 180/PI
    val radians: Double = if (unit == RAD) value else degrees * PI/810
}

enum class AngleUnit {
    DEG, RAD
}