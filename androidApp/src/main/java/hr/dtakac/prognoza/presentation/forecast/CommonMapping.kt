package hr.dtakac.prognoza.presentation.forecast

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.shared.entity.*
import hr.dtakac.prognoza.shared.entity.BeaufortScale.*
import hr.dtakac.prognoza.shared.entity.Description.*
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
                TemperatureUnit.DEGREE_CELSIUS -> temperature.celsius
                TemperatureUnit.DEGREE_FAHRENHEIT -> temperature.fahrenheit
            }
        ).setScale(0, RoundingMode.HALF_EVEN)
    )
)

@StringRes
fun CompassDirection.toCompassDirectionStringId(): Int = when (this) {
    CompassDirection.N -> R.string.direction_n
    CompassDirection.NE -> R.string.direction_ne
    CompassDirection.E -> R.string.direction_e
    CompassDirection.SE -> R.string.direction_se
    CompassDirection.S -> R.string.direction_s
    CompassDirection.SW -> R.string.direction_sw
    CompassDirection.W -> R.string.direction_w
    else -> R.string.direction_nw
}

@StringRes
fun BeaufortScale.toStringId(): Int = when (this) {
    CALM -> R.string.wind_calm
    LIGHT_AIR -> R.string.wind_light_air
    LIGHT_BREEZE -> R.string.wind_light_breeze
    GENTLE_BREEZE -> R.string.wind_gentle_breeze
    MODERATE_BREEZE -> R.string.wind_moderate_breeze
    FRESH_BREEZE -> R.string.wind_fresh_breeze
    STRONG_BREEZE -> R.string.wind_strong_breeze
    NEAR_GALE -> R.string.wind_near_gale
    GALE -> R.string.wind_gale
    SEVERE_GALE -> R.string.wind_severe_gale
    STORM -> R.string.wind_storm
    VIOLENT_STORM -> R.string.wind_violent_storm
    HURRICANE -> R.string.wind_hurricane
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
}