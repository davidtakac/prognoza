package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.TemperatureUnit

class SelectTemperatureUnit internal constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(unit: TemperatureUnit) = settingsRepository.setTemperatureUnit(unit)
}