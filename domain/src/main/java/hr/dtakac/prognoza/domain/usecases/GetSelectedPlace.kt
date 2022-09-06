package hr.dtakac.prognoza.domain.usecases

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.entities.Place

class GetSelectedPlace(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): Place {
        TODO("access user settings, extract the place, etc.")
    }
}