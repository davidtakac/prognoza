package hr.dtakac.prognoza.repository.place

import hr.dtakac.prognoza.database.entity.Place

interface PlaceRepository {
    suspend fun getSelectedPlace(): Place
}