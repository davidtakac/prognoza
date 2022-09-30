package hr.dtakac.prognoza.themesettings.usecase

import hr.dtakac.prognoza.themesettings.ThemeSetting
import hr.dtakac.prognoza.themesettings.repository.ThemeSettingRepository

class GetThemeSetting(
    private val themeSettingRepository: ThemeSettingRepository
) {
    suspend operator fun invoke(): ThemeSetting = themeSettingRepository.getThemeSetting()
}