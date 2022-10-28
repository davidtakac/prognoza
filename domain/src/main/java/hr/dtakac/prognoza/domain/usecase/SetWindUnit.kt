package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit

class SetWindUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(windUnit: SpeedUnit) = settingsRepository.setWindUnit(windUnit)
}