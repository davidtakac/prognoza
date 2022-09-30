package hr.dtakac.prognoza.themesettings.usecase

import hr.dtakac.prognoza.themesettings.ThemeSetting
import hr.dtakac.prognoza.themesettings.repository.ThemeSettingRepository

class SetThemeSetting(
    private val themeSettingRepository: ThemeSettingRepository
) {
    suspend operator fun invoke(themeSetting: ThemeSetting) =
        themeSettingRepository.setThemeSetting(themeSetting)
}