package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place

interface PlaceSaver {
    suspend fun save(place: Place)
}