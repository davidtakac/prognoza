package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

internal interface PlaceSearcher {
    suspend fun search(
        query: String,
        rfc2616Language: String
    ): List<Place>?
}