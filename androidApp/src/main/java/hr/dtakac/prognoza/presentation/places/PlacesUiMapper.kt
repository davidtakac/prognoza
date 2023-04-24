package hr.dtakac.prognoza.presentation.places

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.di.ComputationDispatcher
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.shared.entity.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesUiMapper @Inject constructor(@ComputationDispatcher private val computationDispatcher: CoroutineDispatcher) {
    suspend fun mapToSavedPlacesUi(
        places: List<Place>,
        selectedPlace: Place?,
    ): List<PlaceUi> = mapToPlaceUi(
        places = places,
        selectedPlace = selectedPlace,
        areDeletable = true
    )

    suspend fun mapToSearchResultPlacesUi(
        places: List<Place>
    ): List<PlaceUi> = mapToPlaceUi(
        places = places,
        selectedPlace = null,
        areDeletable = false
    )

    private suspend fun mapToPlaceUi(
        places: List<Place>,
        selectedPlace: Place?,
        areDeletable: Boolean
    ): List<PlaceUi> = withContext(computationDispatcher) {
        places.map {
            PlaceUi(
                name = TextResource.fromString(it.name),
                details = TextResource.fromString(it.details ?: ""),
                isSelected = it == selectedPlace,
                isDeletable = areDeletable && it != selectedPlace
            )
        }
    }

    fun mapToSearchPlacesError(
        result: List<Place>?,
        query: String
    ): TextResource? = when {
        result == null -> TextResource.fromStringId(R.string.places_err_search)
        result.isEmpty() -> TextResource.fromStringId(
            id = R.string.places_err_none_found,
            TextResource.fromString(query)
        )
        else -> null
    }

    fun getEmptyMessage(): TextResource = TextResource.fromStringId(R.string.places_msg_empty)

    fun getProvider(): TextResource = TextResource.fromStringId(
        R.string.common_msg_data_from,
        TextResource.fromStringId(R.string.common_value_data_from_open_meteo)
    )
}