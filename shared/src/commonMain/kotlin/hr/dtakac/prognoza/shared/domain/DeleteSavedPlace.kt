package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SavedPlaceRemover
import hr.dtakac.prognoza.shared.entity.Place

class DeleteSavedPlace internal constructor(
    private val placeDeleter: SavedPlaceRemover
) {
    suspend operator fun invoke(place: Place) = placeDeleter.remove(place)
}