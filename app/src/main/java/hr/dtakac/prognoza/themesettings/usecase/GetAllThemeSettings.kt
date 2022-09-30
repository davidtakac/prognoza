package hr.dtakac.prognoza.themesettings.usecase

import hr.dtakac.prognoza.themesettings.ThemeSetting

class GetAllThemeSettings {
    suspend operator fun invoke(): List<ThemeSetting> = ThemeSetting.values().toList()
}