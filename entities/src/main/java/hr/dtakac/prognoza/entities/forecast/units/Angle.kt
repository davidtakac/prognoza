package hr.dtakac.prognoza.entities.forecast.units

import kotlin.math.PI
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit.*

class Angle(
    value: Double,
    unit: AngleUnit
) {
    val degrees: Double = if (unit == DEG) value else value * 180/PI
    val radians: Double = if (unit == RAD) value else value * PI/180
    val compassDirection: CompassDirection = CompassDirection.fromDegrees(degrees)
}

enum class AngleUnit {
    DEG, RAD
}

enum class CompassDirection {
    N, NE, E, SE, S, SW, W, NW;

    companion object {
        internal fun fromDegrees(degrees: Double): CompassDirection {
            val normalizedDegrees = (degrees % 360 + 360) % 360
            return when {
                normalizedDegrees < 45 -> N
                normalizedDegrees < 90 -> NE
                normalizedDegrees < 135 -> E
                normalizedDegrees < 180 -> SE
                normalizedDegrees < 225 -> S
                normalizedDegrees < 270 -> SW
                normalizedDegrees < 315 -> W
                else -> NW
            }
        }
    }
}