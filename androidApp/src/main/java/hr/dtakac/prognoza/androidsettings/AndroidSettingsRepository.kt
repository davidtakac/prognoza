package hr.dtakac.prognoza.androidsettings

import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import javax.inject.Inject

private const val ThemeSettingKey = "theme_setting_key"
private const val MoodModeKey = "mood_mode_key"

class AndroidSettingsRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun getAvailableUiModes(): List<UiMode> = UiMode.values().toList()

    fun getUiMode(): UiMode {
        val settingValue = sharedPreferences.getString(ThemeSettingKey, null)
        return if (settingValue == null) {
            val default = UiMode.FollowSystem
            setUiMode(default)
            default
        } else {
            UiMode.valueOf(settingValue)
        }
    }

    fun setUiMode(uiMode: UiMode)  {
        sharedPreferences.edit {
            putString(ThemeSettingKey, uiMode.name)
        }
    }

    fun getAvailableMoodModes(): List<MoodMode> = MoodMode.values().toList()

    fun getMoodMode(): MoodMode {
        val settingValue = sharedPreferences.getString(MoodModeKey, null)
        return if (settingValue == null) {
            val default = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MoodMode.Dynamic
            } else MoodMode.Forecast
            setMoodMode(default)
            default
        } else {
            MoodMode.valueOf(settingValue)
        }
    }

    fun setMoodMode(moodMode: MoodMode) {
        sharedPreferences.edit {
            putString(MoodModeKey, moodMode.name)
        }
    }
}