package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.Place

internal interface SavedPlaceRemover {
    suspend fun remove(place: Place)
}