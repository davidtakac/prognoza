package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit

class SetPrecipitationUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(lengthUnit: LengthUnit) =
        settingsRepository.setPrecipitationUnit(lengthUnit)
}