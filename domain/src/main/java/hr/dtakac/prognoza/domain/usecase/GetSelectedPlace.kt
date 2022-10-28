package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.settings.SettingsRepository
import hr.dtakac.prognoza.entities.Place

class GetSelectedPlace(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Place? = settingsRepository.getPlace()
}