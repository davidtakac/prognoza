package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.Place

class GetSelectedPlace(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Place? = settingsRepository.getPlace()
}