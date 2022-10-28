package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

class SetTemperatureUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(unit: TemperatureUnit) {
        settingsRepository.setTemperatureUnit(unit)
    }
}