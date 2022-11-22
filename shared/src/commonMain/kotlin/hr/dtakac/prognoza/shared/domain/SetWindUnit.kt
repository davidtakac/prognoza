package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.SpeedUnit

class SetWindUnit internal constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(windUnit: SpeedUnit) = settingsRepository.setWindUnit(windUnit)
}