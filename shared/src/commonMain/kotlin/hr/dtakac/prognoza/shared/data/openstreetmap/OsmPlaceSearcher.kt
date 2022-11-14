package hr.dtakac.prognoza.shared.data.openstreetmap

import hr.dtakac.prognoza.shared.domain.data.PlaceSearcher
import hr.dtakac.prognoza.shared.domain.data.PlaceSearcherResult
import io.github.aakira.napier.Napier

private val TAG = OsmPlaceSearcher::class.simpleName ?: ""

class OsmPlaceSearcher(
    private val osmPlaceService: OsmPlaceService
) : PlaceSearcher {
    override suspend fun search(
        query: String,
        rfc2616Language: String
    ): PlaceSearcherResult {
        val entities = try {
            osmPlaceService.search(query, rfc2616Language).map(PlaceResponse::toEntity)
        } catch (e: Exception) {
            Napier.e(TAG, e)
            null
        }
        return entities?.let {
            PlaceSearcherResult.Success(it)
        } ?: PlaceSearcherResult.Error
    }
}