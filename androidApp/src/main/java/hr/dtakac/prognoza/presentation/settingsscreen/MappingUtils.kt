package hr.dtakac.prognoza.presentation.settingsscreen

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.shared.entity.*

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.DegreeCelsius -> R.string.settings_label_temperature_celsius
    TemperatureUnit.DegreeFahrenheit -> R.string.settings_label_temperature_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.MetrePerSecond -> R.string.settings_label_speed_mps
    SpeedUnit.KilometrePerHour -> R.string.settings_label_speed_kph
    SpeedUnit.MilePerHour -> R.string.settings_label_speed_mph
    SpeedUnit.Knot -> R.string.settings_label_speed_knots
    SpeedUnit.BeaufortNumber -> R.string.settings_label_speed_beaufort
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.Metric -> R.string.settings_label_length_metric
    LengthUnit.Imperial -> R.string.settings_label_length_imperial
}

@StringRes
fun PressureUnit.toSettingsLabel(): Int = when (this) {
    PressureUnit.Millibar -> R.string.settings_label_pressure_mbar
    PressureUnit.InchOfMercury -> R.string.settings_label_pressure_inhg
}

@StringRes
fun UiMode.toSettingsLabel(): Int = when (this) {
    UiMode.Dark -> R.string.settings_label_theme_dark
    UiMode.Light -> R.string.settings_label_theme_light
    UiMode.FollowSystem -> R.string.settings_label_theme_follow_system
}

@StringRes
fun MoodMode.toSettingsLabel(): Int = when (this) {
    MoodMode.Forecast -> R.string.settings_label_mood_forecast
    MoodMode.Dynamic -> R.string.settings_label_mood_dynamic
}