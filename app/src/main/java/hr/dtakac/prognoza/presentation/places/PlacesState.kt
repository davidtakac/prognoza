package hr.dtakac.prognoza.presentation.places

import hr.dtakac.prognoza.presentation.TextResource

data class PlacesState(
    val isLoading: Boolean = false,
    val empty: PlacesEmpty? = null,
    val places: List<PlaceUi> = listOf()
)

data class PlaceUi(
    val name: TextResource,
    val details: TextResource,
    val isSelected: Boolean
)

sealed interface PlacesEmpty {
    object NoSavedPlaces : PlacesEmpty
    object NoPlacesFoundForQuery : PlacesEmpty
}