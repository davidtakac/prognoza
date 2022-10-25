package hr.dtakac.prognoza.presentation.places

import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.presentation.TextResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PlacesUiMapper @Inject constructor(
    @Named("computation")
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend fun mapToPlaceUi(
        places: List<Place>,
        selectedPlace: Place?
    ): List<PlaceUi> = withContext(computationDispatcher) {
        places.map {
            PlaceUi(
                name = TextResource.fromText(it.name),
                details = TextResource.fromText(it.details ?: ""),
                isSelected = it == selectedPlace
            )
        }
    }
}