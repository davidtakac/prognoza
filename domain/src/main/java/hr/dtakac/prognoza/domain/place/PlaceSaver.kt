package hr.dtakac.prognoza.domain.place

import hr.dtakac.prognoza.entities.Place

interface PlaceSaver {
    suspend fun save(place: Place)
}