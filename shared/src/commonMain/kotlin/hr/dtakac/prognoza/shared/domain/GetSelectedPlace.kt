package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.data.PlaceRepository
import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.Place

class GetSelectedPlace internal constructor(
    private val settingsRepository: SettingsRepository,
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): Place? = settingsRepository.getSelectedPlaceId()?.let { placeRepository.getSavedPlace(it) }
}