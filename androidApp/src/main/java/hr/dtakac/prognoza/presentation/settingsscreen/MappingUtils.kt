package hr.dtakac.prognoza.presentation.settingsscreen

import androidx.annotation.StringRes
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.UiMode

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