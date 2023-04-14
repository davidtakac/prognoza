package hr.dtakac.prognoza.presentation.settingsscreen

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.shared.entity.*

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.DegreeCelsius -> R.string.settings_label_celsius
    TemperatureUnit.DegreeFahrenheit -> R.string.settings_label_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.MetrePerSecond -> R.string.settings_label_mps
    SpeedUnit.KilometrePerHour -> R.string.settings_label_kph
    SpeedUnit.MilePerHour -> R.string.settings_label_mph
    SpeedUnit.Knot -> R.string.settings_label_knots
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.Millimetre -> R.string.settings_label_mm
    LengthUnit.Inch -> R.string.settings_label_in
    LengthUnit.Centimetre -> R.string.settings_label_cm
}

@StringRes
fun PressureUnit.toSettingsLabel(): Int = when (this) {
    PressureUnit.Millibar -> R.string.settings_label_mbar
    PressureUnit.InchOfMercury -> R.string.settings_label_inhg
}

@StringRes
fun UiMode.toSettingsLabel(): Int = when (this) {
    UiMode.DARK -> R.string.settings_label_dark
    UiMode.LIGHT -> R.string.settings_label_light
    UiMode.FOLLOW_SYSTEM -> R.string.settings_label_follow_system
}

@StringRes
fun MoodMode.toSettingsLabel(): Int = when (this) {
    MoodMode.FORECAST -> R.string.mood_mode_forecast
    MoodMode.DYNAMIC -> R.string.mood_mode_dynamic
}

@StringRes
fun ForecastProvider.toSettingsLabel(): Int = when (this) {
    ForecastProvider.MET_NORWAY -> R.string.met_norway_credit
    ForecastProvider.OPEN_METEO -> R.string.open_meteo_credit
}