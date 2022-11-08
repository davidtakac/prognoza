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
    TemperatureUnit.C -> R.string.settings_label_celsius
    TemperatureUnit.F -> R.string.settings_label_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.MPS -> R.string.settings_label_mps
    SpeedUnit.KMH -> R.string.settings_label_kph
    SpeedUnit.MPH -> R.string.settings_label_mph
    SpeedUnit.KN -> R.string.settings_label_knots
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.MM -> R.string.settings_label_mm
    LengthUnit.IN -> R.string.settings_label_in
    LengthUnit.CM -> R.string.settings_label_cm
}

@StringRes
fun PressureUnit.toSettingsLabel(): Int = when (this) {
    PressureUnit.MBAR -> R.string.settings_label_mbar
    PressureUnit.INHG -> R.string.settings_label_inhg
}

@StringRes
fun ThemeSetting.toSettingsLabel(): Int = when (this) {
    ThemeSetting.DARK -> R.string.settings_label_dark
    ThemeSetting.LIGHT -> R.string.settings_label_light
    ThemeSetting.FOLLOW_SYSTEM -> R.string.settings_label_follow_system
}