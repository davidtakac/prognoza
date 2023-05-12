package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.PressureUnit

class SelectPressureUnit internal constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(pressureUnit: PressureUnit) = settingsRepository.setPressureUnit(pressureUnit)
}