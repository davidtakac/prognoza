package hr.dtakac.prognoza.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.glance.LocalContext
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.Description

// Accessing drawables this way instead of putting them in drawable and drawable-night
// dirs is required to make them dynamic based on the current theme. Not scalable at all.
// Another way is to use the AppCompatDelegate.setDefaultNightMode, but this makes it
// impossible to have a nice color change animation between themes because it recreates
// the current activity.
data class WeatherIcons(
    @DrawableRes val ic01d: Int,
    @DrawableRes val ic01m: Int,
    @DrawableRes val ic01n: Int,
    @DrawableRes val ic02d: Int,
    @DrawableRes val ic02m: Int,
    @DrawableRes val ic02n: Int,
    @DrawableRes val ic03d: Int,
    @DrawableRes val ic03m: Int,
    @DrawableRes val ic03n: Int,
    @DrawableRes val ic04: Int,
    @DrawableRes val ic05d: Int,
    @DrawableRes val ic05m: Int,
    @DrawableRes val ic05n: Int,
    @DrawableRes val ic06d: Int,
    @DrawableRes val ic06m: Int,
    @DrawableRes val ic06n: Int,
    @DrawableRes val ic07d: Int,
    @DrawableRes val ic07m: Int,
    @DrawableRes val ic07n: Int,
    @DrawableRes val ic08d: Int,
    @DrawableRes val ic08m: Int,
    @DrawableRes val ic08n: Int,
    @DrawableRes val ic09: Int,
    @DrawableRes val ic10: Int,
    @DrawableRes val ic11: Int,
    @DrawableRes val ic12: Int,
    @DrawableRes val ic13: Int,
    @DrawableRes val ic14: Int,
    @DrawableRes val ic15: Int,
    @DrawableRes val ic20d: Int,
    @DrawableRes val ic20m: Int,
    @DrawableRes val ic20n: Int,
    @DrawableRes val ic21d: Int,
    @DrawableRes val ic21m: Int,
    @DrawableRes val ic21n: Int,
    @DrawableRes val ic22: Int,
    @DrawableRes val ic23: Int,
    @DrawableRes val ic24d: Int,
    @DrawableRes val ic24m: Int,
    @DrawableRes val ic24n: Int,
    @DrawableRes val ic25d: Int,
    @DrawableRes val ic25m: Int,
    @DrawableRes val ic25n: Int,
    @DrawableRes val ic26d: Int,
    @DrawableRes val ic26m: Int,
    @DrawableRes val ic26n: Int,
    @DrawableRes val ic27d: Int,
    @DrawableRes val ic27m: Int,
    @DrawableRes val ic27n: Int,
    @DrawableRes val ic28d: Int,
    @DrawableRes val ic28m: Int,
    @DrawableRes val ic28n: Int,
    @DrawableRes val ic29d: Int,
    @DrawableRes val ic29m: Int,
    @DrawableRes val ic29n: Int,
    @DrawableRes val ic30: Int,
    @DrawableRes val ic31: Int,
    @DrawableRes val ic32: Int,
    @DrawableRes val ic33: Int,
    @DrawableRes val ic34: Int,
    @DrawableRes val ic40d: Int,
    @DrawableRes val ic40m: Int,
    @DrawableRes val ic40n: Int,
    @DrawableRes val ic41d: Int,
    @DrawableRes val ic41m: Int,
    @DrawableRes val ic41n: Int,
    @DrawableRes val ic42d: Int,
    @DrawableRes val ic42m: Int,
    @DrawableRes val ic42n: Int,
    @DrawableRes val ic43d: Int,
    @DrawableRes val ic43m: Int,
    @DrawableRes val ic43n: Int,
    @DrawableRes val ic44d: Int,
    @DrawableRes val ic44m: Int,
    @DrawableRes val ic44n: Int,
    @DrawableRes val ic45d: Int,
    @DrawableRes val ic45m: Int,
    @DrawableRes val ic45n: Int,
    @DrawableRes val ic46: Int,
    @DrawableRes val ic47: Int,
    @DrawableRes val ic48: Int,
    @DrawableRes val ic49: Int,
    @DrawableRes val ic50: Int
) {
    companion object {
        fun get(useDarkTheme: Boolean) = if (!useDarkTheme) lightWeatherIcons else darkWeatherIcons
    }
}

private val darkWeatherIcons = WeatherIcons(
    ic01d = R.drawable.dark_01d,
    ic01m = R.drawable.dark_01m,
    ic01n = R.drawable.dark_01n,
    ic02d = R.drawable.dark_02d,
    ic02m = R.drawable.dark_02m,
    ic02n = R.drawable.dark_02n,
    ic03d = R.drawable.dark_03d,
    ic03m = R.drawable.dark_03m,
    ic03n = R.drawable.dark_03n,
    ic04 = R.drawable.dark_04,
    ic05d = R.drawable.dark_05d,
    ic05m = R.drawable.dark_05m,
    ic05n = R.drawable.dark_05n,
    ic06d = R.drawable.dark_06d,
    ic06m = R.drawable.dark_06m,
    ic06n = R.drawable.dark_06n,
    ic07d = R.drawable.dark_07d,
    ic07m = R.drawable.dark_07m,
    ic07n = R.drawable.dark_07n,
    ic08d = R.drawable.dark_08d,
    ic08m = R.drawable.dark_08m,
    ic08n = R.drawable.dark_08n,
    ic09 = R.drawable.dark_09,
    ic10 = R.drawable.dark_10,
    ic11 = R.drawable.dark_11,
    ic12 = R.drawable.dark_12,
    ic13 = R.drawable.dark_13,
    ic14 = R.drawable.dark_14,
    ic15 = R.drawable.dark_15,
    ic20d = R.drawable.dark_20d,
    ic20m = R.drawable.dark_20m,
    ic20n = R.drawable.dark_20n,
    ic21d = R.drawable.dark_21d,
    ic21m = R.drawable.dark_21m,
    ic21n = R.drawable.dark_21n,
    ic22 = R.drawable.dark_22,
    ic23 = R.drawable.dark_23,
    ic24d = R.drawable.dark_24d,
    ic24m = R.drawable.dark_24m,
    ic24n = R.drawable.dark_24n,
    ic25d = R.drawable.dark_25d,
    ic25m = R.drawable.dark_25m,
    ic25n = R.drawable.dark_25n,
    ic26d = R.drawable.dark_26d,
    ic26m = R.drawable.dark_26m,
    ic26n = R.drawable.dark_26n,
    ic27d = R.drawable.dark_27d,
    ic27m = R.drawable.dark_27m,
    ic27n = R.drawable.dark_27n,
    ic28d = R.drawable.dark_28d,
    ic28m = R.drawable.dark_28m,
    ic28n = R.drawable.dark_28n,
    ic29d = R.drawable.dark_29d,
    ic29m = R.drawable.dark_29m,
    ic29n = R.drawable.dark_29n,
    ic30 = R.drawable.dark_30,
    ic31 = R.drawable.dark_31,
    ic32 = R.drawable.dark_32,
    ic33 = R.drawable.dark_33,
    ic34 = R.drawable.dark_34,
    ic40d = R.drawable.dark_40d,
    ic40m = R.drawable.dark_40m,
    ic40n = R.drawable.dark_40n,
    ic41d = R.drawable.dark_41d,
    ic41m = R.drawable.dark_41m,
    ic41n = R.drawable.dark_41n,
    ic42d = R.drawable.dark_42d,
    ic42m = R.drawable.dark_42m,
    ic42n = R.drawable.dark_42n,
    ic43d = R.drawable.dark_43d,
    ic43m = R.drawable.dark_43m,
    ic43n = R.drawable.dark_43n,
    ic44d = R.drawable.dark_44d,
    ic44m = R.drawable.dark_44m,
    ic44n = R.drawable.dark_44n,
    ic45d = R.drawable.dark_45d,
    ic45m = R.drawable.dark_45m,
    ic45n = R.drawable.dark_45n,
    ic46 = R.drawable.dark_46,
    ic47 = R.drawable.dark_47,
    ic48 = R.drawable.dark_48,
    ic49 = R.drawable.dark_49,
    ic50 = R.drawable.dark_50
)

private val lightWeatherIcons = WeatherIcons(
    ic01d = R.drawable.light_01d,
    ic01m = R.drawable.light_01m,
    ic01n = R.drawable.light_01n,
    ic02d = R.drawable.light_02d,
    ic02m = R.drawable.light_02m,
    ic02n = R.drawable.light_02n,
    ic03d = R.drawable.light_03d,
    ic03m = R.drawable.light_03m,
    ic03n = R.drawable.light_03n,
    ic04 = R.drawable.light_04,
    ic05d = R.drawable.light_05d,
    ic05m = R.drawable.light_05m,
    ic05n = R.drawable.light_05n,
    ic06d = R.drawable.light_06d,
    ic06m = R.drawable.light_06m,
    ic06n = R.drawable.light_06n,
    ic07d = R.drawable.light_07d,
    ic07m = R.drawable.light_07m,
    ic07n = R.drawable.light_07n,
    ic08d = R.drawable.light_08d,
    ic08m = R.drawable.light_08m,
    ic08n = R.drawable.light_08n,
    ic09 = R.drawable.light_09,
    ic10 = R.drawable.light_10,
    ic11 = R.drawable.light_11,
    ic12 = R.drawable.light_12,
    ic13 = R.drawable.light_13,
    ic14 = R.drawable.light_14,
    ic15 = R.drawable.light_15,
    ic20d = R.drawable.light_20d,
    ic20m = R.drawable.light_20m,
    ic20n = R.drawable.light_20n,
    ic21d = R.drawable.light_21d,
    ic21m = R.drawable.light_21m,
    ic21n = R.drawable.light_21n,
    ic22 = R.drawable.light_22,
    ic23 = R.drawable.light_23,
    ic24d = R.drawable.light_24d,
    ic24m = R.drawable.light_24m,
    ic24n = R.drawable.light_24n,
    ic25d = R.drawable.light_25d,
    ic25m = R.drawable.light_25m,
    ic25n = R.drawable.light_25n,
    ic26d = R.drawable.light_26d,
    ic26m = R.drawable.light_26m,
    ic26n = R.drawable.light_26n,
    ic27d = R.drawable.light_27d,
    ic27m = R.drawable.light_27m,
    ic27n = R.drawable.light_27n,
    ic28d = R.drawable.light_28d,
    ic28m = R.drawable.light_28m,
    ic28n = R.drawable.light_28n,
    ic29d = R.drawable.light_29d,
    ic29m = R.drawable.light_29m,
    ic29n = R.drawable.light_29n,
    ic30 = R.drawable.light_30,
    ic31 = R.drawable.light_31,
    ic32 = R.drawable.light_32,
    ic33 = R.drawable.light_33,
    ic34 = R.drawable.light_34,
    ic40d = R.drawable.light_40d,
    ic40m = R.drawable.light_40m,
    ic40n = R.drawable.light_40n,
    ic41d = R.drawable.light_41d,
    ic41m = R.drawable.light_41m,
    ic41n = R.drawable.light_41n,
    ic42d = R.drawable.light_42d,
    ic42m = R.drawable.light_42m,
    ic42n = R.drawable.light_42n,
    ic43d = R.drawable.light_43d,
    ic43m = R.drawable.light_43m,
    ic43n = R.drawable.light_43n,
    ic44d = R.drawable.light_44d,
    ic44m = R.drawable.light_44m,
    ic44n = R.drawable.light_44n,
    ic45d = R.drawable.light_45d,
    ic45m = R.drawable.light_45m,
    ic45n = R.drawable.light_45n,
    ic46 = R.drawable.light_46,
    ic47 = R.drawable.light_47,
    ic48 = R.drawable.light_48,
    ic49 = R.drawable.light_49,
    ic50 = R.drawable.light_50
)

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
}

@Composable
@DrawableRes
fun Description.asGlanceWeatherIconResId(
    useDarkTheme: Boolean = LocalContext.current.let {
        try {
            it.resources.configuration.isNightModeActive
        } catch (_: NoSuchMethodError) {
            false
        }
    }
): Int = asWeatherIconResId(WeatherIcons.get(useDarkTheme))