package hr.dtakac.prognoza.shared.entity

import kotlin.math.PI
import hr.dtakac.prognoza.shared.entity.AngleUnit.*

class Angle(
    value: Double,
    unit: AngleUnit
) {
    val degree: Double = if (unit == DEGREE) value else value * 180/PI
    val radian: Double = if (unit == RADIAN) value else value * PI/180
    val compassDirection: CompassDirection = CompassDirection.fromDegrees(degree)
}

enum class AngleUnit {
    DEGREE, RADIAN
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