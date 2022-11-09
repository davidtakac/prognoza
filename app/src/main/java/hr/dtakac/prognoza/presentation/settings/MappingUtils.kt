package hr.dtakac.prognoza.presentation.settings

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.theme.ThemeSetting

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.DEGREE_CELSIUS -> R.string.settings_label_celsius
    TemperatureUnit.DEGREE_FAHRENHEIT -> R.string.settings_label_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.METER_PER_SECOND -> R.string.settings_label_mps
    SpeedUnit.KILOMETER_PER_HOUR -> R.string.settings_label_kph
    SpeedUnit.MILE_PER_HOUR -> R.string.settings_label_mph
    SpeedUnit.KNOT -> R.string.settings_label_knots
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.MILLIMETER -> R.string.settings_label_mm
    LengthUnit.INCH -> R.string.settings_label_in
    LengthUnit.CENTIMETER -> R.string.settings_label_cm
}

@StringRes
fun PressureUnit.toSettingsLabel(): Int = when (this) {
    PressureUnit.MILLIBAR -> R.string.settings_label_mbar
    PressureUnit.INCH_OF_MERCURY -> R.string.settings_label_inhg
}

@StringRes
fun ThemeSetting.toSettingsLabel(): Int = when (this) {
    ThemeSetting.DARK -> R.string.settings_label_dark
    ThemeSetting.LIGHT -> R.string.settings_label_light
    ThemeSetting.FOLLOW_SYSTEM -> R.string.settings_label_follow_system
}