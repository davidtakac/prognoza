package hr.dtakac.prognoza.presentation.settingsscreen

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.androidsettings.model.MoodMode
import hr.dtakac.prognoza.androidsettings.model.ThemeSetting
import hr.dtakac.prognoza.shared.entity.LengthUnit
import hr.dtakac.prognoza.shared.entity.PressureUnit
import hr.dtakac.prognoza.shared.entity.SpeedUnit
import hr.dtakac.prognoza.shared.entity.TemperatureUnit

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.DEGREE_CELSIUS -> R.string.settings_label_celsius
    TemperatureUnit.DEGREE_FAHRENHEIT -> R.string.settings_label_fahrenheit
}

@StringRes
fun SpeedUnit.toSettingsLabel(): Int = when (this) {
    SpeedUnit.METRE_PER_SECOND -> R.string.settings_label_mps
    SpeedUnit.KILOMETRE_PER_HOUR -> R.string.settings_label_kph
    SpeedUnit.MILE_PER_HOUR -> R.string.settings_label_mph
    SpeedUnit.KNOT -> R.string.settings_label_knots
}

@StringRes
fun LengthUnit.toSettingsLabel(): Int = when (this) {
    LengthUnit.MILLIMETRE -> R.string.settings_label_mm
    LengthUnit.INCH -> R.string.settings_label_in
    LengthUnit.CENTIMETRE -> R.string.settings_label_cm
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

@StringRes
fun MoodMode.toSettingsLabel(): Int = when (this) {
    MoodMode.FORECAST -> R.string.mood_mode_forecast
    MoodMode.DYNAMIC -> R.string.mood_mode_dynamic
}