package hr.dtakac.prognoza.presentation.theme

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val THEME_SETTING_KEY = "theme_setting_key"
class SharedPrefsThemeSettingRepository @Inject constructor(
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
}