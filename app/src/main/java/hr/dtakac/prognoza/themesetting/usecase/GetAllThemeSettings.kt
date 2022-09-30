package hr.dtakac.prognoza.themesetting.usecase

import hr.dtakac.prognoza.themesetting.ThemeSetting

class GetAllThemeSettings {
    suspend operator fun invoke(): List<ThemeSetting> = ThemeSetting.values().toList()
}