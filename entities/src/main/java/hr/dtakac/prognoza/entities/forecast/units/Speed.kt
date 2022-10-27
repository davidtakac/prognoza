package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException
import hr.dtakac.prognoza.entities.forecast.units.BeaufortScale.*

class Speed(
    value: Double,
    unit: SpeedUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Speed must be >= 0, was $value.")
        }
        if (unit == SpeedUnit.BEAUFORT) {
            throw IllegalStateException("Cannot initialize speed in Beaufort scale. Use an exact unit, such as m/s.")
        }
    }

    val metersPerSecond: Double = when (unit) {
        SpeedUnit.MPS -> value
        SpeedUnit.KPH -> value / 3.6
        SpeedUnit.MPH -> value / 2.2369
        SpeedUnit.KNOTS -> value / 1.9438
        else -> throw IllegalStateException("Unsupported unit.")
    }
    val kilometersPerHour: Double = if (unit == SpeedUnit.KPH) value else metersPerSecond * 3.6
    val milesPerHour: Double = if (unit == SpeedUnit.MPH) value else metersPerSecond * 2.2369
    val knots: Double = if (unit == SpeedUnit.KNOTS) value else metersPerSecond * 1.9438

    // Beaufort scale: https://www.weather.gov/mfl/beaufort
    val beaufort: BeaufortScale = when {
        milesPerHour < 1 -> CALM
        milesPerHour < 3 -> LIGHT_AIR
        milesPerHour < 7 -> LIGHT_BREEZE
        milesPerHour < 12 -> GENTLE_BREEZE
        milesPerHour < 18 -> MODERATE_BREEZE
        milesPerHour < 24 -> FRESH_BREEZE
        milesPerHour < 31 -> STRONG_BREEZE
        milesPerHour < 38 -> NEAR_GALE
        milesPerHour < 46 -> GALE
        milesPerHour < 54 -> SEVERE_GALE
        milesPerHour < 63 -> STORM
        milesPerHour < 72 -> VIOLENT_STORM
        else -> HURRICANE
    }
}

enum class SpeedUnit {
    MPS, KPH, MPH, KNOTS, BEAUFORT
}

enum class BeaufortScale {
    CALM,
    LIGHT_AIR,
    LIGHT_BREEZE,
    GENTLE_BREEZE,
    MODERATE_BREEZE,
    FRESH_BREEZE,
    STRONG_BREEZE,
    NEAR_GALE,
    GALE,
    SEVERE_GALE,
    STORM,
    VIOLENT_STORM,
    HURRICANE
}