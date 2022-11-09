package hr.dtakac.prognoza.entities.forecast.units

import hr.dtakac.prognoza.entities.forecast.units.BeaufortScale.*

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
        SpeedUnit.METRE_PER_SECOND -> value
        SpeedUnit.KILOMETRE_PER_HOUR -> value / 3.6
        SpeedUnit.MILE_PER_HOUR -> value / 2.2369
        SpeedUnit.KNOT -> value / 1.9438
    }
    val kilometersPerHour: Double = if (unit == SpeedUnit.KILOMETRE_PER_HOUR) value else metersPerSecond * 3.6
    val milesPerHour: Double = if (unit == SpeedUnit.MILE_PER_HOUR) value else metersPerSecond * 2.2369
    val knots: Double = if (unit == SpeedUnit.KNOT) value else metersPerSecond * 1.9438
    val beaufortScale: BeaufortScale = Companion.fromMilesPerHour(milesPerHour)
}

enum class SpeedUnit {
    METRE_PER_SECOND,
    KILOMETRE_PER_HOUR,
    MILE_PER_HOUR,
    KNOT
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
    HURRICANE;

    companion object {
        // Beaufort scale: https://www.weather.gov/mfl/beaufort
        internal fun fromMilesPerHour(milesPerHour: Double): BeaufortScale = when {
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
}