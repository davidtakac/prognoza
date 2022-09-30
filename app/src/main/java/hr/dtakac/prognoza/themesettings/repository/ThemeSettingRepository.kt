package hr.dtakac.prognoza.themesettings.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import hr.dtakac.prognoza.themesettings.ThemeSetting
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ThemeSettingRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getThemeSetting(): ThemeSetting {
        val settingOrdinal = sharedPreferences.getInt(THEME_SETTING_KEY, -1)
        return if (settingOrdinal == -1) {
            val default = ThemeSetting.FOLLOW_SYSTEM
            setThemeSetting(default)
            default
        } else {
            ThemeSetting.values()[settingOrdinal]
        }
    }

    suspend fun setThemeSetting(themeSetting: ThemeSetting) {
        return suspendCoroutine { cont ->
            sharedPreferences.edit(commit = true) {
                putInt(THEME_SETTING_KEY, themeSetting.ordinal)
            }
            cont.resume(Unit)
        }
    }
}

private const val THEME_SETTING_KEY = "theme_setting_key"