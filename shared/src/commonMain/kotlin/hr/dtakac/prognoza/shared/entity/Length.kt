package hr.dtakac.prognoza.shared.entity

class Length private constructor(
    val value: Double,
    val unit: LengthUnit
) {
    init {
        if (value < 0) throwInvalidLength()
    }

    override fun toString(): String = "$value ${unit.suffix}"

    // From ChatGPT 3.5 for the prompt
    // "Can you map meteorological visibility in kilometres to qualitative measurements?"
    val visibility: Visibility = unit.convert(value, LengthUnit.Kilometre).let { kilometres ->
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

    private fun throwInvalidLength(): Nothing =
        throw IllegalStateException("Length must be positive, was ${toString()}.")

    companion object {
        fun fromSnowfall(
            value: Double,
            unit: LengthUnit,
            system: MeasurementSystem
        ): Length {
            val targetUnit = when (system) {
                MeasurementSystem.Metric -> LengthUnit.Centimetre
                MeasurementSystem.Imperial -> LengthUnit.Inch
            }
            return Length(unit.convert(value, targetUnit), targetUnit)
        }

        fun fromRainfall(
            value: Double,
            unit: LengthUnit,
            system: MeasurementSystem
        ): Length {
            val targetUnit = when (system) {
                MeasurementSystem.Metric -> LengthUnit.Millimetre
                MeasurementSystem.Imperial -> LengthUnit.Inch
            }
            return Length(unit.convert(value, targetUnit), targetUnit)
        }

        fun fromVisibility(
            value: Double,
            unit: LengthUnit,
            system: MeasurementSystem
        ): Length {
            val targetUnit = when (system) {
                MeasurementSystem.Metric ->
                    if (unit.convert(value, LengthUnit.Metre) >= 1000) LengthUnit.Kilometre
                    else LengthUnit.Metre
                MeasurementSystem.Imperial -> LengthUnit.Inch
            }
            return Length(unit.convert(value, targetUnit), targetUnit)
        }
    }
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
    Mile(metres = 1609.34, suffix = "mi");

    fun convert(
        value: Double,
        targetUnit: LengthUnit
    ): Double = if (this == targetUnit) value else value * metres / targetUnit.metres
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