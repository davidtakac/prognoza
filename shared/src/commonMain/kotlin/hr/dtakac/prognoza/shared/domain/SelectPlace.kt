package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.PlaceSaver
import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.Place

class SelectPlace(
    private val placeSaver: PlaceSaver,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(place: Place) {
        placeSaver.save(place)
        settingsRepository.setPlace(place)
    }
}