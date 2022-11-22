package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.LengthUnit

class SetPrecipitationUnit internal constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(lengthUnit: LengthUnit) =
        settingsRepository.setPrecipitationUnit(lengthUnit)
}