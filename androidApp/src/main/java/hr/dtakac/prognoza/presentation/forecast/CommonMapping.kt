package hr.dtakac.prognoza.presentation.forecast

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.shared.entity.*
import java.math.BigDecimal
import java.math.RoundingMode

fun getTemperature(
    temperature: Temperature,
    unit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_temperature_degrees,
    TextResource.fromNumber(
        BigDecimal(
            when (unit) {
                TemperatureUnit.DegreeCelsius -> temperature.degreesCelsius
                TemperatureUnit.DegreeFahrenheit -> temperature.degreesFahrenheit
            }
        ).setScale(0, RoundingMode.HALF_EVEN)
    )
)

@StringRes
fun CardinalDirection.toCompassDirectionStringId(): Int = when (this) {
    CardinalDirection.N -> R.string.direction_n
    CardinalDirection.NE -> R.string.direction_ne
    CardinalDirection.E -> R.string.direction_e
    CardinalDirection.SE -> R.string.direction_se
    CardinalDirection.S -> R.string.direction_s
    CardinalDirection.SW -> R.string.direction_sw
    CardinalDirection.W -> R.string.direction_w
    else -> R.string.direction_nw
}

@StringRes
fun Int.toBeaufortStringId(): Int = when (this) {
    0 -> R.string.wind_calm
    1 -> R.string.wind_light_air
    2 -> R.string.wind_light_breeze
    3 -> R.string.wind_gentle_breeze
    4 -> R.string.wind_moderate_breeze
    5 -> R.string.wind_fresh_breeze
    6 -> R.string.wind_strong_breeze
    7 -> R.string.wind_near_gale
    8 -> R.string.wind_gale
    9 -> R.string.wind_severe_gale
    11 -> R.string.wind_storm
    12 -> R.string.wind_violent_storm
    else -> R.string.wind_hurricane
}

/*
// See https://open-meteo.com/en/docs for mapping
private fun Int.toWmoCodeDrawableResId(isDay: Boolean): Int = when (this) {
    0 -> when (isDay) {
        true -> Description.CLEAR_SKY_DAY
        false -> Description.CLEAR_SKY_NIGHT
    }

    1 -> when (isDay) {
        true -> Description.FAIR_DAY
        false -> Description.FAIR_NIGHT
    }

    2 -> when (isDay) {
        true -> Description.PARTLY_CLOUDY_DAY
        false -> Description.PARTLY_CLOUDY_NIGHT
    }

    3 -> Description.CLOUDY

    45, // Fog
    48  // Depositing rime fog
    -> Description.FOG

    51, // Light drizzle
    56, // Light freezing drizzle
    61, // Slight rain
    66  // Light freezing rain
    -> Description.LIGHT_RAIN

    53, // Moderate drizzle
    63  // Moderate rain
    -> Description.RAIN

    55, // Dense drizzle
    57, // Dense freezing drizzle
    65, // Heavy rain
    67  // Heavy freezing rain
    -> Description.HEAVY_RAIN

    71 -> Description.LIGHT_SNOW
    73, // Moderate snow
    77  // Snow grains
    -> Description.SNOW
    75 -> Description.HEAVY_SNOW

    80 -> when (isDay) {
        true -> Description.LIGHT_RAIN_SHOWERS_DAY
        false -> Description.LIGHT_RAIN_SHOWERS_NIGHT
    }
    81 -> when (isDay) {
        true -> Description.RAIN_SHOWERS_DAY
        false -> Description.RAIN_SHOWERS_NIGHT
    }
    82 -> when (isDay) {
        true -> Description.HEAVY_RAIN_SHOWERS_DAY
        false -> Description.HEAVY_RAIN_SHOWERS_NIGHT
    }

    85 -> when (isDay) {
        true -> Description.LIGHT_SNOW_SHOWERS_DAY
        false -> Description.LIGHT_SNOW_SHOWERS_NIGHT
    }

    86 -> when (isDay) {
        true -> Description.HEAVY_SNOW_SHOWERS_DAY
        false -> Description.HEAVY_SNOW_SHOWERS_NIGHT
    }

    95 -> Description.RAIN_AND_THUNDER
    96 -> Description.LIGHT_SLEET_AND_THUNDER
    99 -> Description.HEAVY_SLEET_AND_THUNDER

    else -> Description.UNKNOWN
}

@StringRes
fun Description.toStringId(): Int = when (this) {
    UNKNOWN -> R.string.description_unknown
    CLEAR_SKY_DAY,
    CLEAR_SKY_NIGHT,
    CLEAR_SKY_POLAR_TWILIGHT -> R.string.description_clear_sky
    CLOUDY -> R.string.description_cloudy
    FAIR_DAY,
    FAIR_NIGHT,
    FAIR_POLAR_TWILIGHT -> R.string.description_fair
    FOG -> R.string.description_fog
    HEAVY_RAIN_AND_THUNDER -> R.string.description_heavy_rain_and_thunder
    HEAVY_RAIN -> R.string.description_heavy_rain
    HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY,
    HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT,
    HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_heavy_rain_showers_and_thunder
    HEAVY_RAIN_SHOWERS_DAY,
    HEAVY_RAIN_SHOWERS_NIGHT,
    HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT -> R.string.description_heavy_rain_showers
    HEAVY_SLEET_AND_THUNDER -> R.string.description_heavy_sleet_and_thunder
    HEAVY_SLEET -> R.string.description_heavy_sleet
    HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY,
    HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT,
    HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_heavy_sleet_showers_and_thunder
    HEAVY_SLEET_SHOWERS_DAY,
    HEAVY_SLEET_SHOWERS_NIGHT,
    HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT -> R.string.description_heavy_sleet_showers
    HEAVY_SNOW_AND_THUNDER -> R.string.description_heavy_snow_and_thunder
    HEAVY_SNOW -> R.string.description_heavy_snow
    HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY,
    HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT,
    HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_heavy_snow_showers_and_thunder
    HEAVY_SNOW_SHOWERS_DAY,
    HEAVY_SNOW_SHOWERS_NIGHT,
    HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT -> R.string.description_heavy_snow_showers
    LIGHT_RAIN_AND_THUNDER -> R.string.description_light_rain_and_thunder
    LIGHT_RAIN -> R.string.description_light_rain
    LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY,
    LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT,
    LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_light_rain_showers_and_thunder
    LIGHT_RAIN_SHOWERS_DAY,
    LIGHT_RAIN_SHOWERS_NIGHT,
    LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT -> R.string.description_light_rain_showers
    LIGHT_SLEET_AND_THUNDER -> R.string.description_light_sleet_and_thunder
    LIGHT_SLEET -> R.string.description_light_sleet
    LIGHT_SLEET_SHOWERS_DAY,
    LIGHT_SLEET_SHOWERS_NIGHT,
    LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT -> R.string.description_light_sleet_showers
    LIGHT_SNOW_AND_THUNDER -> R.string.description_light_snow_and_thunder
    LIGHT_SNOW -> R.string.description_light_snow
    LIGHT_SNOW_SHOWERS_DAY,
    LIGHT_SNOW_SHOWERS_NIGHT,
    LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT -> R.string.description_light_snow_showers
    LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY,
    LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT,
    LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_light_sleet_showers_and_thunder
    LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY,
    LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT,
    LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_light_snow_showers_and_thunder
    PARTLY_CLOUDY_DAY,
    PARTLY_CLOUDY_NIGHT,
    PARTLY_CLOUDY_POLAR_TWILIGHT -> R.string.description_partly_cloudy
    RAIN_AND_THUNDER -> R.string.description_rain_and_thunder
    RAIN -> R.string.description_rain
    RAIN_SHOWERS_AND_THUNDER_DAY,
    RAIN_SHOWERS_AND_THUNDER_NIGHT,
    RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_rain_showers_and_thunder
    RAIN_SHOWERS_DAY,
    RAIN_SHOWERS_NIGHT,
    RAIN_SHOWERS_POLAR_TWILIGHT -> R.string.description_rain_showers
    SLEET_AND_THUNDER -> R.string.description_sleet_and_thunder
    SLEET -> R.string.description_sleet
    SLEET_SHOWERS_AND_THUNDER_DAY,
    SLEET_SHOWERS_AND_THUNDER_NIGHT,
    SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_sleet_showers_and_thunder
    SLEET_SHOWERS_DAY,
    SLEET_SHOWERS_NIGHT,
    SLEET_SHOWERS_POLAR_TWILIGHT -> R.string.description_sleet_showers
    SNOW_AND_THUNDER -> R.string.description_snow_and_thunder
    SNOW -> R.string.description_snow
    SNOW_SHOWERS_AND_THUNDER_DAY,
    SNOW_SHOWERS_AND_THUNDER_NIGHT,
    SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.string.description_snow_showers_and_thunder
    SNOW_SHOWERS_DAY,
    SNOW_SHOWERS_NIGHT,
    SNOW_SHOWERS_POLAR_TWILIGHT -> R.string.description_snow_showers
}*/
