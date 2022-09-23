package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.Place

class GetSelectedPlace(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Place? = /*Place("Tenja", null, 45.5, 18.7)*/settingsRepository.getPlace()
}