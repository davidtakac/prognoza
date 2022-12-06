package hr.dtakac.prognoza.androidsettings

import android.content.SharedPreferences
import androidx.core.content.edit
import hr.dtakac.prognoza.androidsettings.model.MoodMode
import hr.dtakac.prognoza.androidsettings.model.ThemeSetting
import javax.inject.Inject

private const val THEME_SETTING_KEY = "theme_setting_key"
private const val MOOD_MODE_KEY = "mood_mode_key"

class AndroidSettingsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getAvailableThemes(): List<ThemeSetting> = ThemeSetting.values().toList()

    fun getTheme(): ThemeSetting {
        val settingValue = sharedPreferences.getString(THEME_SETTING_KEY, null)
        return if (settingValue == null) {
            val default = ThemeSetting.FOLLOW_SYSTEM
            setTheme(default)
            default
        } else {
            ThemeSetting.valueOf(settingValue)
        }
    }

    fun setTheme(themeSetting: ThemeSetting)  {
        sharedPreferences.edit {
            putString(THEME_SETTING_KEY, themeSetting.name)
        }
    }

    fun getAvailableMoodModes(): List<MoodMode> = MoodMode.values().toList()

    fun getMoodMode(): MoodMode {
        val settingValue = sharedPreferences.getString(MOOD_MODE_KEY, null)
        return if (settingValue == null) {
            val default = MoodMode.FORECAST
            setMoodMode(default)
            default
        } else {
            MoodMode.valueOf(settingValue)
        }
    }

    fun setMoodMode(moodMode: MoodMode) {
        sharedPreferences.edit {
            putString(MOOD_MODE_KEY, moodMode.name)
        }
    }
}