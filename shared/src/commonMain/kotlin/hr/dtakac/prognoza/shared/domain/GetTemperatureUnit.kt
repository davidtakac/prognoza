package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.TemperatureUnit

class GetTemperatureUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): TemperatureUnit = settingsRepository.getTemperatureUnit()
}