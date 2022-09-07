package hr.dtakac.prognoza.entities.forecast.wind

import hr.dtakac.prognoza.entities.forecast.units.Angle
import hr.dtakac.prognoza.entities.forecast.units.Speed
import hr.dtakac.prognoza.entities.forecast.wind.WindDescription.*

data class Wind(
    val speed: Speed,
    val fromDirection: Angle
) {
    // Beaufort scale: https://www.weather.gov/mfl/beaufort
    val description: WindDescription = when {
        speed.milesPerHour < 1 -> CALM
        speed.milesPerHour < 3 -> LIGHT_AIR
        speed.milesPerHour < 7 -> LIGHT_BREEZE
        speed.milesPerHour < 12 -> GENTLE_BREEZE
        speed.milesPerHour < 18 -> MODERATE_BREEZE
        speed.milesPerHour < 24 -> FRESH_BREEZE
        speed.milesPerHour < 31 -> STRONG_BREEZE
        speed.milesPerHour < 38 -> NEAR_GALE
        speed.milesPerHour < 46 -> GALE
        speed.milesPerHour < 54 -> SEVERE_GALE
        speed.milesPerHour < 63 -> STORM
        speed.milesPerHour < 72 -> VIOLENT_STORM
        else -> HURRICANE
    }
}