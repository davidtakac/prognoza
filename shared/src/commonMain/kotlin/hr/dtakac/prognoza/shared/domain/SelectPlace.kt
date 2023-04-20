package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.PlaceRepository
import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.Place

class SelectPlace internal constructor(
    private val placeRepository: PlaceRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(place: Place) {
        placeRepository.save(place)
        settingsRepository.setPlace(place)
    }
}