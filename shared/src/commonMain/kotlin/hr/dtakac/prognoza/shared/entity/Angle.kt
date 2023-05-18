package hr.dtakac.prognoza.shared.entity

import kotlin.math.PI

class Angle internal constructor(
    val value: Double,
    val unit: AngleUnit
) {
    override fun toString(): String = "$value ${unit.suffix}"

    val cardinalDirection: CardinalDirection =
        ((valueIn(AngleUnit.Degree) % 360 + 360) % 360).let { normalizedDegrees ->
            when {
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

    fun convertTo(targetUnit: AngleUnit): Angle = Angle(valueIn(targetUnit), targetUnit)

    private fun valueIn(targetUnit: AngleUnit): Double =
        if (targetUnit == unit) value
        else value * unit.radians / targetUnit.radians
}

enum class AngleUnit(
    internal val radians: Double,
    internal val suffix: String
) {
    Radian(radians = 1.0, suffix = "rad"),
    Degree(radians = PI / 180, suffix = "°")
}

enum class CardinalDirection {
    N, NE, E, SE, S, SW, W, NW;
}