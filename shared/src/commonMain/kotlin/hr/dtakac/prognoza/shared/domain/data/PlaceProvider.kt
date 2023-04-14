package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

internal interface PlaceProvider {
    suspend fun get(query: String, rfc2616Language: String): List<Place>?
}