package hr.dtakac.prognoza.osmplacesearcher

import hr.dtakac.prognoza.domain.place.PlaceSearcher
import hr.dtakac.prognoza.domain.place.PlaceSearcherResult
import io.github.aakira.napier.Napier

private const val TAG = "OsmPlaceSearcher"

class OsmPlaceSearcher(
    private val osmPlaceService: OsmPlaceService
) : PlaceSearcher {
    override suspend fun search(query: String): PlaceSearcherResult {
        val entities = try {
            osmPlaceService.search(query).map(PlaceResponse::toEntity)
        } catch (e: Exception) {
            Napier.e(TAG, e)
            null
        }
        return entities?.let {
            PlaceSearcherResult.Success(it)
        } ?: PlaceSearcherResult.Error
    }
}