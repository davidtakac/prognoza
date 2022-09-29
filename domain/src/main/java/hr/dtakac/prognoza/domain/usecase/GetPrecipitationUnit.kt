package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit

class GetPrecipitationUnit(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): LengthUnit = settingsRepository.getPrecipitationUnit()
}