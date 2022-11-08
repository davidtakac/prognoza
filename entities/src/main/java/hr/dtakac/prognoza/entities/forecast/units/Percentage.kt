package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Percentage(
    value: Double,
    unit: PercentageUnit
) {
    init {
        val percent = calculatePercent(value, unit)
        if (percent !in 0.0..100.0) {
            throw IllegalStateException("Value in percent must be in [0, 100], was $percent.")
        }
    }

    val percent: Double = calculatePercent(value, unit)

    private fun calculatePercent(
        value: Double,
        unit: PercentageUnit
    ): Double = if (unit == PercentageUnit.PERCENT) value else value * 100
}

enum class PercentageUnit {
    PERCENT, FRACTION
}