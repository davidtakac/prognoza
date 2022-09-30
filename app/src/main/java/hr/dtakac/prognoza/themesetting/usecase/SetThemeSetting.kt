package hr.dtakac.prognoza.themesetting.usecase

import hr.dtakac.prognoza.themesetting.ThemeSetting
import hr.dtakac.prognoza.themesetting.repository.ThemeSettingRepository

class SetThemeSetting(
    private val themeSettingRepository: ThemeSettingRepository
) {
    suspend operator fun invoke(themeSetting: ThemeSetting) =
        themeSettingRepository.setThemeSetting(themeSetting)
}