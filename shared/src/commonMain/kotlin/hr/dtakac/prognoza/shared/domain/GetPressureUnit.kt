package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.PressureUnit

class GetPressureUnit internal constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): PressureUnit = settingsRepository.getPressureUnit()
}