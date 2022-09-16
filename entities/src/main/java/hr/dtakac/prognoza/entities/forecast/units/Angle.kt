package hr.dtakac.prognoza.entities.forecast.units

import kotlin.math.PI
import hr.dtakac.prognoza.entities.forecast.units.AngleUnit.*

class Angle(
    value: Double,
    unit: AngleUnit
) {
    val degrees: Double = if (unit == DEG) value else value * 180/PI
    val compassDirection: CompassDirection = when (degrees) {
        in 0.0..45.0 -> CompassDirection.N
        in 45.0..90.0 -> CompassDirection.NE
        in 90.0..135.0 -> CompassDirection.E
        in 135.0..180.0 -> CompassDirection.SE
        in 180.0..225.0 -> CompassDirection.S
        in 225.0..270.0 -> CompassDirection.SW
        in 270.0..315.0 -> CompassDirection.W
        in 315.0..360.0 -> CompassDirection.NW
        else -> throw IllegalStateException("Angle exceeds 360 degrees.")
    }
}

enum class AngleUnit {
    DEG, RAD
}

enum class CompassDirection {
    N, NE, E, SE, S, SW, W, NW
}