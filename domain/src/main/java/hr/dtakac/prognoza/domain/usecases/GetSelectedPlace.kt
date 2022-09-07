package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.SettingsRepository
import hr.dtakac.prognoza.entities.Place

class GetSelectedPlace(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(): Place? = Place("Tenja", null, 45.0, 18.0) // todo: replace with this settingsRepository.getPlace()
}