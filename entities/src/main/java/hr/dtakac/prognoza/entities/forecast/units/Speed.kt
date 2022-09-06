package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Speed(
    private val value: Double,
    private val unit: SpeedUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Speed must be >= 0, was $value.")
        }
    }

    val metersPerSecond: Double = when (unit) {
        SpeedUnit.MPS -> value
        SpeedUnit.KPH -> value / 3.6
        SpeedUnit.MPH -> value / 2.2369
    }
    val kilometersPerHour: Double = if (unit == SpeedUnit.KPH) value else metersPerSecond * 3.6
    val milesPerHour: Double = if (unit == SpeedUnit.MPH) value else metersPerSecond * 2.2369
}

enum class SpeedUnit {
    MPS, KPH, MPH
}