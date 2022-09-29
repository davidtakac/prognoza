package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

class GetTemperatureUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): TemperatureUnit = settingsRepository.getTemperatureUnit()
}