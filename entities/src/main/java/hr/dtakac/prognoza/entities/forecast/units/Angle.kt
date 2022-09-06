package hr.dtakac.prognoza.entities.forecast.units

import kotlin.math.roundToInt
import hr.dtakac.prognoza.entities.forecast.units.CompassDirection.*
import kotlin.math.PI
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit.*

class Angle(
    private val value: Double,
    private val unit: AngleUnit
) {
    val degrees: Double = if (unit == DEG) value else value * 180/PI
    val compassDirection: CompassDirection = resolveCompassDirection()

    private fun resolveCompassDirection(): CompassDirection {
        val normalizedAngle = (degrees + (degrees % 360) + 360) % 360
        return when (normalizedAngle.roundToInt()) {
            in 0..44 -> N
            in 45..89 -> NE
            in 90..134 -> E
            in 135..179 -> SE
            in 180..224 -> S
            in 225..269 -> SW
            in 270..314 -> W
            else -> N
        }
    }
}

enum class AngleUnit {
    DEG, RAD
}

enum class CompassDirection {
    N, NE, E, SE, S, SW, W
}