package hr.dtakac.prognoza.presentation.forecast

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.Wind
import hr.dtakac.prognoza.presentation.TextResource
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZonedDateTime
import hr.dtakac.prognoza.entities.forecast.Description
import hr.dtakac.prognoza.entities.forecast.Description.*
import hr.dtakac.prognoza.entities.forecast.units.BeaufortScale
import hr.dtakac.prognoza.entities.forecast.units.BeaufortScale.*

fun getLowHighTemperature(
    lowTemperature: Temperature,
    highTemperature: Temperature,
    temperatureUnit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_high_low_temperature,
    getTemperature(highTemperature, temperatureUnit),
    getTemperature(lowTemperature, temperatureUnit)
)

fun getShortTime(time: ZonedDateTime): TextResource = TextResource.fromShortTime(
    time.toInstant().toEpochMilli()
)

fun getTemperature(
    temperature: Temperature,
    unit: TemperatureUnit
): TextResource = TextResource.fromStringId(
    id = R.string.template_temperature_degrees,
    TextResource.fromNumber(
        BigDecimal(
            when (unit) {
                TemperatureUnit.C -> temperature.celsius
                TemperatureUnit.F -> temperature.fahrenheit
            }
        ).setScale(0, RoundingMode.HALF_EVEN)
    )
)

fun getWind(
    wind: Wind,
    unit: SpeedUnit
): TextResource {
    fun BigDecimal.format() = setScale(0, RoundingMode.HALF_EVEN)
    return when (unit) {
        SpeedUnit.KMH -> TextResource.fromStringId(
            R.string.template_wind_kmh,
            TextResource.fromNumber(BigDecimal(wind.speed.kilometersPerHour).format())
        )
        SpeedUnit.MPH -> TextResource.fromStringId(
            R.string.template_wind_mph,
            TextResource.fromNumber(BigDecimal(wind.speed.milesPerHour).format())
        )
        SpeedUnit.MPS -> TextResource.fromStringId(
            R.string.template_wind_mps,
            TextResource.fromNumber(BigDecimal(wind.speed.metersPerSecond).format())
        )
        SpeedUnit.KT -> TextResource.fromStringId(
            R.string.template_wind_knots,
            TextResource.fromNumber(BigDecimal(wind.speed.knots).format())
        )
    }
}

fun getPrecipitation(
    precipitation: Length,
    unit: LengthUnit
): TextResource {
    val zero = BigDecimal(0.0)
    fun BigDecimal.format() = setScale(1, RoundingMode.HALF_EVEN)
    return when (unit) {
        LengthUnit.MM -> BigDecimal(precipitation.millimeters)
            .format()
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_mm,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()

        LengthUnit.IN -> BigDecimal(precipitation.inches)
            .format()
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_in,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()

        LengthUnit.CM -> BigDecimal(precipitation.centimeters)
            .format()
            .takeUnless { it.compareTo(zero) == 0 }
            ?.let {
                TextResource.fromStringId(
                    id = R.string.template_precipitation_cm,
                    TextResource.fromNumber(it)
                )
            }
            ?: TextResource.empty()
    }
}

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

@DrawableRes
fun Description.toDrawableId(): Int = when (this) {
    UNKNOWN -> R.drawable.ic_question_mark
    CLEAR_SKY_DAY -> R.drawable.clearsky_day
    CLEAR_SKY_NIGHT -> R.drawable.clearsky_night
    CLEAR_SKY_POLAR_TWILIGHT -> R.drawable.clearsky_polartwilight
    CLOUDY -> R.drawable.cloudy
    FAIR_DAY -> R.drawable.fair_day
    FAIR_NIGHT -> R.drawable.fair_night
    FAIR_POLAR_TWILIGHT -> R.drawable.fair_polartwilight
    FOG -> R.drawable.fog
    HEAVY_RAIN_AND_THUNDER -> R.drawable.heavyrainandthunder
    HEAVY_RAIN -> R.drawable.heavyrain
    HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY -> R.drawable.heavyrainshowersandthunder_day
    HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.heavyrainshowersandthunder_night
    HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.heavyrainshowersandthunder_polartwilight
    HEAVY_RAIN_SHOWERS_DAY -> R.drawable.heavyrainshowers_day
    HEAVY_RAIN_SHOWERS_NIGHT -> R.drawable.heavyrainshowers_night
    HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT -> R.drawable.heavyrainshowers_polartwilight
    HEAVY_SLEET_AND_THUNDER -> R.drawable.heavysleetandthunder
    HEAVY_SLEET -> R.drawable.heavysleet
    HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY -> R.drawable.heavysleetshowersandthunder_day
    HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.heavysleetshowersandthunder_night
    HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.heavysleetshowersandthunder_polartwilight
    HEAVY_SLEET_SHOWERS_DAY -> R.drawable.heavysleetshowers_day
    HEAVY_SLEET_SHOWERS_NIGHT -> R.drawable.heavysleetshowers_night
    HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT -> R.drawable.heavysleetshowers_polartwilight
    HEAVY_SNOW_AND_THUNDER -> R.drawable.heavysnowandthunder
    HEAVY_SNOW -> R.drawable.heavysnow
    HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY -> R.drawable.heavysnowshowersandthunder_day
    HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.heavysnowshowersandthunder_night
    HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.heavysnowshowersandthunder_polartwilight
    HEAVY_SNOW_SHOWERS_DAY -> R.drawable.heavysnowshowers_day
    HEAVY_SNOW_SHOWERS_NIGHT -> R.drawable.heavysnowshowers_night
    HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT -> R.drawable.heavysnowshowers_polartwilight
    LIGHT_RAIN_AND_THUNDER -> R.drawable.lightrainandthunder
    LIGHT_RAIN -> R.drawable.lightrain
    LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY -> R.drawable.lightrainshowersandthunder_day
    LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.lightrainshowersandthunder_night
    LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.lightrainshowersandthunder_polartwilight
    LIGHT_RAIN_SHOWERS_DAY -> R.drawable.lightrainshowers_day
    LIGHT_RAIN_SHOWERS_NIGHT -> R.drawable.lightrainshowers_night
    LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT -> R.drawable.lightrainshowers_polartwilight
    LIGHT_SLEET_AND_THUNDER -> R.drawable.lightsleetandthunder
    LIGHT_SLEET -> R.drawable.lightsleet
    LIGHT_SLEET_SHOWERS_DAY -> R.drawable.lightsleetshowers_day
    LIGHT_SLEET_SHOWERS_NIGHT -> R.drawable.lightsleetshowers_night
    LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT -> R.drawable.lightsleetshowers_polartwilight
    LIGHT_SNOW_AND_THUNDER -> R.drawable.lightsnowandthunder
    LIGHT_SNOW -> R.drawable.lightsnow
    LIGHT_SNOW_SHOWERS_DAY -> R.drawable.lightsnowshowers_day
    LIGHT_SNOW_SHOWERS_NIGHT -> R.drawable.lightsnowshowers_night
    LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT -> R.drawable.lightsnowshowers_polartwilight
    LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY -> R.drawable.lightssleetshowersandthunder_day
    LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.lightssleetshowersandthunder_night
    LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.lightssleetshowersandthunder_polartwilight
    LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY -> R.drawable.lightssnowshowersandthunder_day
    LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.lightssnowshowersandthunder_night
    LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.lightssnowshowersandthunder_polartwilight
    PARTLY_CLOUDY_DAY -> R.drawable.partlycloudy_day
    PARTLY_CLOUDY_NIGHT -> R.drawable.partlycloudy_night
    PARTLY_CLOUDY_POLAR_TWILIGHT -> R.drawable.partlycloudy_polartwilight
    RAIN_AND_THUNDER -> R.drawable.rainandthunder
    RAIN -> R.drawable.rain
    RAIN_SHOWERS_AND_THUNDER_DAY -> R.drawable.rainshowersandthunder_day
    RAIN_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.rainshowersandthunder_night
    RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.rainshowersandthunder_polartwilight
    RAIN_SHOWERS_DAY -> R.drawable.rainshowers_day
    RAIN_SHOWERS_NIGHT -> R.drawable.rainshowers_night
    RAIN_SHOWERS_POLAR_TWILIGHT -> R.drawable.rainshowers_polartwilight
    SLEET_AND_THUNDER -> R.drawable.sleetandthunder
    SLEET -> R.drawable.sleet
    SLEET_SHOWERS_AND_THUNDER_DAY -> R.drawable.sleetshowersandthunder_day
    SLEET_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.sleetshowersandthunder_night
    SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.sleetshowersandthunder_polartwilight
    SLEET_SHOWERS_DAY -> R.drawable.sleetshowers_day
    SLEET_SHOWERS_NIGHT -> R.drawable.sleetshowers_night
    SLEET_SHOWERS_POLAR_TWILIGHT -> R.drawable.sleetshowers_polartwilight
    SNOW_AND_THUNDER -> R.drawable.snowandthunder
    SNOW -> R.drawable.snow
    SNOW_SHOWERS_AND_THUNDER_DAY -> R.drawable.snowshowersandthunder_day
    SNOW_SHOWERS_AND_THUNDER_NIGHT -> R.drawable.snowshowersandthunder_night
    SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> R.drawable.snowshowersandthunder_polartwilight
    SNOW_SHOWERS_DAY -> R.drawable.snowshowers_day
    SNOW_SHOWERS_NIGHT -> R.drawable.snowshowers_night
    SNOW_SHOWERS_POLAR_TWILIGHT -> R.drawable.snowshowers_polartwilight
}