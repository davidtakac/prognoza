package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.LengthUnit

class GetPrecipitationUnit internal constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(): LengthUnit = settingsRepository.getPrecipitationUnit()
}