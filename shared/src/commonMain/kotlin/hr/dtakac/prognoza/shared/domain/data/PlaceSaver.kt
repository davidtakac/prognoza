package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

internal interface PlaceSaver {
    suspend fun save(place: Place)
}