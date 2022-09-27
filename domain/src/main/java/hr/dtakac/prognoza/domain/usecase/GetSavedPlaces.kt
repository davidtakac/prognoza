package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.domain.repository.PlaceRepository
import hr.dtakac.prognoza.domain.repository.PlaceRepositoryResult
import hr.dtakac.prognoza.entities.Place

class GetSavedPlaces(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<Place> {
        val results = placeRepository.getSaved()
        return if (results is PlaceRepositoryResult.Success) results.data else listOf()
    }
}