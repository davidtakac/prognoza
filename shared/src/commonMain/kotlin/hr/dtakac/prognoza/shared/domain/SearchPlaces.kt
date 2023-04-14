package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.PlaceProvider
import hr.dtakac.prognoza.shared.entity.Place
import hr.dtakac.prognoza.shared.platform.LocalRfc2616LanguageGetter

class SearchPlaces internal constructor(
    private val placeProvider: PlaceProvider,
    private val localRfc2616LanguageGetter: LocalRfc2616LanguageGetter
) {
    suspend operator fun invoke(query: String): List<Place>? = placeProvider.get(
        query = query,
        rfc2616Language = localRfc2616LanguageGetter.get()
    )
}