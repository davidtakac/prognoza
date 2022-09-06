package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Percentage(
    private val value: Double,
    private val unit: PercentageUnit
) {
    init {
        val percent = resolvePercent()
        if (percent !in 0.0..100.0) {
            throw IllegalStateException("Value in percent must be in [0, 100], was $percent.")
        }
    }

    val percent: Double = resolvePercent()
    val fraction: Double = if (unit == PercentageUnit.FRACTION) value else percent / 100

    private fun resolvePercent(): Double = if (unit == PercentageUnit.PERCENT) value else value * 100
}

enum class PercentageUnit {
    PERCENT, FRACTION
}