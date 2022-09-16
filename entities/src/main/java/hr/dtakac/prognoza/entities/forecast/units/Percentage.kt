package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Percentage(
    value: Double,
    unit: PercentageUnit
) {
    init {
        val percent = resolvePercent(value, unit)
        if (percent !in 0.0..100.0) {
            throw IllegalStateException("Value in percent must be in [0, 100], was $percent.")
        }
    }

    val percent: Double = resolvePercent(value, unit)

    private fun resolvePercent(
        value: Double,
        unit: PercentageUnit
    ): Double = if (unit == PercentageUnit.PERCENT) value else value * 100
}

enum class PercentageUnit {
    PERCENT, FRACTION
}