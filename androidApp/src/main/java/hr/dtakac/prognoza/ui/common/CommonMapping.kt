package hr.dtakac.prognoza.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.*

@StringRes
fun CardinalDirection.toCompassDirectionStringId(): Int = when (this) {
    CardinalDirection.N -> R.string.cardinal_n
    CardinalDirection.NE -> R.string.cardinal_ne
    CardinalDirection.E -> R.string.cardinal_e
    CardinalDirection.SE -> R.string.cardinal_se
    CardinalDirection.S -> R.string.cardinal_s
    CardinalDirection.SW -> R.string.cardinal_sw
    CardinalDirection.W -> R.string.cardinal_w
    else -> R.string.cardinal_nw
}

@StringRes
fun Int.toBeaufortStringId(): Int = when (this) {
    0 -> R.string.beaufort_label_0
    1 -> R.string.beaufort_label_1
    2 -> R.string.beaufort_label_2
    3 -> R.string.beaufort_label_3
    4 -> R.string.beaufort_label_4
    5 -> R.string.beaufort_label_5
    6 -> R.string.beaufort_label_6
    7 -> R.string.beaufort_label_7
    8 -> R.string.beaufort_label_8
    9 -> R.string.beaufort_label_9
    11 -> R.string.beaufort_label_11
    12 -> R.string.beaufort_label_12
    else -> R.string.beaufort_label_13
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
/*

// See https://nrkno.github.io/yr-weather-symbols/ for the mapping logic
@Composable
@DrawableRes
fun Description.asWeatherIconResId(
    weatherIcons: WeatherIcons = PrognozaTheme.weatherIcons
): Int = when (this) {
    Description.UNKNOWN -> R.drawable.ic_question_mark
    Description.CLEAR_SKY_DAY -> weatherIcons.ic01d
    Description.CLEAR_SKY_NIGHT -> weatherIcons.ic01n
    Description.CLEAR_SKY_POLAR_TWILIGHT -> weatherIcons.ic01m
    Description.CLOUDY -> weatherIcons.ic04
    Description.FAIR_DAY -> weatherIcons.ic02d
    Description.FAIR_NIGHT -> weatherIcons.ic02n
    Description.FAIR_POLAR_TWILIGHT -> weatherIcons.ic02m
    Description.FOG -> weatherIcons.ic15
    Description.HEAVY_RAIN_AND_THUNDER -> weatherIcons.ic11
    Description.HEAVY_RAIN -> weatherIcons.ic10
    Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic25d
    Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic25n
    Description.HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic25m
    Description.HEAVY_RAIN_SHOWERS_DAY -> weatherIcons.ic41d
    Description.HEAVY_RAIN_SHOWERS_NIGHT -> weatherIcons.ic41n
    Description.HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic41m
    Description.HEAVY_SLEET_AND_THUNDER -> weatherIcons.ic32
    Description.HEAVY_SLEET -> weatherIcons.ic48
    Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic27d
    Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic27n
    Description.HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic27m
    Description.HEAVY_SLEET_SHOWERS_DAY -> weatherIcons.ic43d
    Description.HEAVY_SLEET_SHOWERS_NIGHT -> weatherIcons.ic43n
    Description.HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic43m
    Description.HEAVY_SNOW_AND_THUNDER -> weatherIcons.ic34
    Description.HEAVY_SNOW -> weatherIcons.ic50
    Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic29d
    Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic29n
    Description.HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic29m
    Description.HEAVY_SNOW_SHOWERS_DAY -> weatherIcons.ic45d
    Description.HEAVY_SNOW_SHOWERS_NIGHT -> weatherIcons.ic45n
    Description.HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic45m
    Description.LIGHT_RAIN_AND_THUNDER -> weatherIcons.ic30
    Description.LIGHT_RAIN -> weatherIcons.ic46
    Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic24d
    Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic24n
    Description.LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic24m
    Description.LIGHT_RAIN_SHOWERS_DAY -> weatherIcons.ic40d
    Description.LIGHT_RAIN_SHOWERS_NIGHT -> weatherIcons.ic40n
    Description.LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic40m
    Description.LIGHT_SLEET_AND_THUNDER -> weatherIcons.ic31
    Description.LIGHT_SLEET -> weatherIcons.ic47
    Description.LIGHT_SLEET_SHOWERS_DAY -> weatherIcons.ic42d
    Description.LIGHT_SLEET_SHOWERS_NIGHT -> weatherIcons.ic42n
    Description.LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic42m
    Description.LIGHT_SNOW_AND_THUNDER -> weatherIcons.ic33
    Description.LIGHT_SNOW -> weatherIcons.ic49
    Description.LIGHT_SNOW_SHOWERS_DAY -> weatherIcons.ic44d
    Description.LIGHT_SNOW_SHOWERS_NIGHT -> weatherIcons.ic44n
    Description.LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic44m
    Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic26d
    Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic26n
    Description.LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic26m
    Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic28d
    Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic28n
    Description.LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic28m
    Description.PARTLY_CLOUDY_DAY -> weatherIcons.ic03d
    Description.PARTLY_CLOUDY_NIGHT -> weatherIcons.ic03n
    Description.PARTLY_CLOUDY_POLAR_TWILIGHT -> weatherIcons.ic03m
    Description.RAIN_AND_THUNDER -> weatherIcons.ic11
    Description.RAIN -> weatherIcons.ic09
    Description.RAIN_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic06d
    Description.RAIN_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic06n
    Description.RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic06m
    Description.RAIN_SHOWERS_DAY -> weatherIcons.ic05d
    Description.RAIN_SHOWERS_NIGHT -> weatherIcons.ic05n
    Description.RAIN_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic05m
    Description.SLEET_AND_THUNDER -> weatherIcons.ic23
    Description.SLEET -> weatherIcons.ic12
    Description.SLEET_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic20d
    Description.SLEET_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic20n
    Description.SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic20m
    Description.SLEET_SHOWERS_DAY -> weatherIcons.ic07d
    Description.SLEET_SHOWERS_NIGHT -> weatherIcons.ic07n
    Description.SLEET_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic07m
    Description.SNOW_AND_THUNDER -> weatherIcons.ic14
    Description.SNOW -> weatherIcons.ic13
    Description.SNOW_SHOWERS_AND_THUNDER_DAY -> weatherIcons.ic21d
    Description.SNOW_SHOWERS_AND_THUNDER_NIGHT -> weatherIcons.ic21n
    Description.SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT -> weatherIcons.ic21m
    Description.SNOW_SHOWERS_DAY -> weatherIcons.ic08d
    Description.SNOW_SHOWERS_NIGHT -> weatherIcons.ic08n
    Description.SNOW_SHOWERS_POLAR_TWILIGHT -> weatherIcons.ic08m
}*/

// TODO: swap with new icons
@DrawableRes
fun wmoCodeToWeatherIcon(wmoCode: Int, day: Boolean) =
    when (wmoCode) {
        0, 1 -> if (day) R.drawable.dark_01d else R.drawable.dark_01n
        2 -> if (day) R.drawable.dark_03d else R.drawable.dark_03n
        3 -> R.drawable.dark_04
        45, 48 -> R.drawable.dark_15
        51, 53, 55, 56, 57 -> R.drawable.dark_46
        61, 63, 65, 66, 67 -> R.drawable.dark_09
        71, 73, 75, 77 -> R.drawable.dark_13
        80, 81, 82 -> if (day) R.drawable.dark_05d else R.drawable.dark_05n
        85, 86 -> if (day) R.drawable.dark_08d else R.drawable.dark_08n
        95, 96, 99 -> if (day) R.drawable.dark_06d else R.drawable.dark_06n
        else -> throw java.lang.IllegalStateException("Unrecognized WMO code: $wmoCode.")
    }

// TODO: Create a new string resource for every one of these WMO codes
@StringRes
fun wmoCodeToWeatherDescription(wmoCode: Int) =
    when (wmoCode) {
        0, 1 -> R.string.description_clear_sky
        2 -> R.string.description_partly_cloudy
        3 -> R.string.description_cloudy
        45, 48 -> R.string.description_fog
        51, 53, 55, 56, 57 -> R.string.description_light_rain
        61, 63, 65, 66, 67 -> R.string.description_rain
        71, 73, 75, 77 -> R.string.description_snow
        80, 81, 82 -> R.string.description_rain_showers
        85, 86 -> R.string.description_snow_showers
        95, 96, 99 -> R.string.description_rain_showers_and_thunder
        else -> throw java.lang.IllegalStateException("Unrecognized WMO code: $wmoCode.")
    }