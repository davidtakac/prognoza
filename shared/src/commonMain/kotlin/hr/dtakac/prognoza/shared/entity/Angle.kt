package hr.dtakac.prognoza.shared.entity

class Angle internal constructor(val degrees: Double) {
    val cardinalDirection: CardinalDirection = calculateCardinalDirection()

    private fun calculateCardinalDirection(): CardinalDirection {
        val normalizedDegrees = (degrees % 360 + 360) % 360
        return when {
            normalizedDegrees < 45 -> CardinalDirection.N
            normalizedDegrees < 90 -> CardinalDirection.NE
            normalizedDegrees < 135 -> CardinalDirection.E
            normalizedDegrees < 180 -> CardinalDirection.SE
            normalizedDegrees < 225 -> CardinalDirection.S
            normalizedDegrees < 270 -> CardinalDirection.SW
            normalizedDegrees < 315 -> CardinalDirection.W
            else -> CardinalDirection.NW
        }
    }
}

enum class AngleUnit {
    Degree, Compass
}

enum class CardinalDirection {
    N, NE, E, SE, S, SW, W, NW;
}