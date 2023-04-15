package hr.dtakac.prognoza.shared.entity

import kotlin.math.PI

class Angle(val degrees: Double) {
    val radians: Double = this.degrees * PI/180
    val compassDirection: CompassDirection = CompassDirection.fromDegrees(this.degrees)
}

enum class AngleUnit {
    Degree, Radian, Compass
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