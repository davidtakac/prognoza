package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.PlaceRepository
import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
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