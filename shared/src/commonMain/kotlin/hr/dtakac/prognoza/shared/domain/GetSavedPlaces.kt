package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SavedPlaceGetter
import hr.dtakac.prognoza.shared.entity.Place

class GetSavedPlaces(private val savedPlaceGetter: SavedPlaceGetter) {
    suspend operator fun invoke(): List<Place> {
        return savedPlaceGetter.getAll()
    }
}