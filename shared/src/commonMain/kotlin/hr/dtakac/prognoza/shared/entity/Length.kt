package hr.dtakac.prognoza.shared.entity

class Length internal constructor(
    val value: Double,
    val unit: LengthUnit
) : Comparable<Length> {
    init {
        if (value < 0) throwInvalidLength()
    }

    override fun compareTo(other: Length): Int {
        val thisMetres = valueIn(LengthUnit.Metre)
        val otherMetres = other.valueIn(LengthUnit.Metre)
        return thisMetres.compareTo(otherMetres)
    }

    override fun toString(): String = "$value ${unit.suffix}"

    fun convertTo(targetUnit: LengthUnit): Length = Length(valueIn(targetUnit), targetUnit)

    // From ChatGPT 3.5 for the prompt
    // "Can you map meteorological visibility in kilometres to qualitative measurements?"
    val visibility: Visibility = valueIn(LengthUnit.Kilometre).let { kilometres ->
        when {
            kilometres >= 10 -> Visibility.Excellent
            kilometres >= 5 -> Visibility.VeryGood
            kilometres >= 3 -> Visibility.Good
            kilometres >= 1 -> Visibility.Moderate
            kilometres >= 0.5 -> Visibility.Poor
            kilometres >= 0.2 -> Visibility.VeryPoor
            else -> Visibility.ExtremelyPoor
        }
    }

    private fun valueIn(targetUnit: LengthUnit): Double =
        if (targetUnit == unit) value
        else value * unit.metres / targetUnit.metres

    private fun throwInvalidLength(): Nothing =
        throw IllegalStateException("Length must be positive, was ${toString()}.")
}

enum class LengthUnit(
    internal val metres: Double,
    internal val suffix: String
) {
    Metre(metres = 1.0, suffix = "m"),
    Millimetre(metres = 0.001, suffix = "mm"),
    Centimetre(metres = 0.01, suffix = "cm"),
    Kilometre(metres = 1000.0, suffix = "km"),
    Inch(metres = 0.0254, suffix = "in"),
    Foot(metres = 0.3048, suffix = "ft"),
    Mile(metres = 1609.34, suffix = "mi")
}

enum class Visibility {
    Excellent,
    VeryGood,
    Good,
    Moderate,
    Poor,
    VeryPoor,
    ExtremelyPoor
}