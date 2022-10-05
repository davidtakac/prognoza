package hr.dtakac.prognoza.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val THEME_SETTING_KEY = "theme_setting_key"

enum class ThemeSetting {
    DARK, LIGHT, FOLLOW_SYSTEM
}

class ThemeChanger(
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun getAvailableThemes(): List<ThemeSetting> = ThemeSetting.values().toList()

    suspend fun getCurrentTheme(): ThemeSetting {
        val settingOrdinal = sharedPreferences.getInt(THEME_SETTING_KEY, -1)
        return if (settingOrdinal == -1) {
            val default = ThemeSetting.FOLLOW_SYSTEM
            setTheme(default)
            default
        } else {
            ThemeSetting.values()[settingOrdinal]
        }
    }

    suspend fun setTheme(themeSetting: ThemeSetting) = withContext(ioDispatcher) {
        suspendCoroutine { cont ->
            sharedPreferences.edit(commit = true) {
                putInt(THEME_SETTING_KEY, themeSetting.ordinal)
            }
            cont.resume(Unit)
        }
    }
}