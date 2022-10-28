package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.place.SavedPlaceGetter
import hr.dtakac.prognoza.entities.Place

class GetSavedPlaces(private val savedPlaceGetter: SavedPlaceGetter) {
    suspend operator fun invoke(): List<Place> {
        return savedPlaceGetter.getAll()
    }
}