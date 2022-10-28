package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.PlaceGetter
import hr.dtakac.prognoza.entities.Place

class GetSavedPlaces(private val placeGetter: PlaceGetter) {
    suspend operator fun invoke(): List<Place> {
        return placeGetter.getAll()
    }
}