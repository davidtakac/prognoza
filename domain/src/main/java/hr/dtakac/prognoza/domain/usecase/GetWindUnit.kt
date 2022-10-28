package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit

class GetWindUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): SpeedUnit = settingsRepository.getWindUnit()
}