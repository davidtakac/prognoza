package hr.dtakac.prognoza.osmplacesprovider

import hr.dtakac.prognoza.domain.place.PlaceSearcher
import hr.dtakac.prognoza.domain.place.PlaceSearcherResult
import java.util.*

class OsmPlaceSearcher(
    private val userAgent: String,
    private val placeService: PlaceService
) : PlaceSearcher {
    override suspend fun search(query: String): PlaceSearcherResult {
        val entities = try {
            placeService.search(
                userAgent = userAgent,
                acceptLanguage = Locale.getDefault().language,
                format = "jsonv2",
                query = query
            ).map(PlaceResponse::toEntity)
        } catch (e: Exception) {
            // todo: implement logging
            null
        }
        return entities?.let {
            PlaceSearcherResult.Success(it)
        } ?: PlaceSearcherResult.Error
    }
}