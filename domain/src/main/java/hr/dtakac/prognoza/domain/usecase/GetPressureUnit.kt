package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit

class GetPressureUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): PressureUnit = settingsRepository.getPressureUnit()
}