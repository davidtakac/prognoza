package hr.dtakac.prognoza.presentation.settings

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.themesetting.ThemeSetting

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.C -> R.string.settings_label_celsius
    TemperatureUnit.F -> R.string.settings_label_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.MPS -> R.string.settings_label_mps
    SpeedUnit.KPH -> R.string.settings_label_kph
    SpeedUnit.MPH -> R.string.settings_label_mph
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.MM -> R.string.settings_label_mm
    LengthUnit.IN -> R.string.settings_label_in
}

@StringRes
fun PressureUnit.toSettingsLabel(): Int = when (this) {
    PressureUnit.HPA -> R.string.settings_label_hpa
    PressureUnit.PSI -> R.string.settings_label_psi
    PressureUnit.BAR -> R.string.settings_label_bar
    PressureUnit.ATM -> R.string.settings_label_atm
}

@StringRes
fun ThemeSetting.toSettingsLabel(): Int = when (this) {
    ThemeSetting.DARK -> R.string.settings_label_dark
    ThemeSetting.LIGHT -> R.string.settings_label_light
    ThemeSetting.FOLLOW_SYSTEM -> R.string.settings_label_follow_system
}