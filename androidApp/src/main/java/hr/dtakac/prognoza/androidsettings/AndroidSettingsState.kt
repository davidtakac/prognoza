package hr.dtakac.prognoza.androidsettings

import hr.dtakac.prognoza.androidsettings.model.MoodMode
import hr.dtakac.prognoza.androidsettings.model.ThemeSetting

data class AndroidSettingsState(
    val theme: ThemeSetting,
    val moodMode: MoodMode
)