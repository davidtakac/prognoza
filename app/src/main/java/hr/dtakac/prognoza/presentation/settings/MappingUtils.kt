package hr.dtakac.prognoza.presentation.settings

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

@StringRes
fun TemperatureUnit.toSettingsLabel(): Int = when (this) {
    TemperatureUnit.C -> R.string.settings_label_celsius
    TemperatureUnit.F -> R.string.settings_label_fahrenheit
}