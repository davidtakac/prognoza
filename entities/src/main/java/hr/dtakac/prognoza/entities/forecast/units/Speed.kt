package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Speed(
    value: Double,
    unit: SpeedUnit
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
        SpeedUnit.KNOTS -> value / 1.9438
    }
    val kilometersPerHour: Double = if (unit == SpeedUnit.KPH) value else metersPerSecond * 3.6
    val milesPerHour: Double = if (unit == SpeedUnit.MPH) value else metersPerSecond * 2.2369
    val knots: Double = if (unit == SpeedUnit.KNOTS) value else metersPerSecond * 1.9438
}

enum class SpeedUnit {
    MPS, KPH, MPH, KNOTS
}