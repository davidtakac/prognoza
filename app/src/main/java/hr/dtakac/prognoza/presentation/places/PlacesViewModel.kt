package hr.dtakac.prognoza.presentation.places

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.GetSavedPlaces
import hr.dtakac.prognoza.domain.usecase.GetSelectedPlace
import hr.dtakac.prognoza.domain.usecase.SearchPlaces
import hr.dtakac.prognoza.domain.usecase.SelectPlace
import hr.dtakac.prognoza.entities.Place
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlaces: SearchPlaces,
    private val getSavedPlaces: GetSavedPlaces,
    private val selectPlace: SelectPlace,
    private val getSelectedPlace: GetSelectedPlace
) : ViewModel() {
    private var currentPlaces: List<Place> = listOf()

    private val _state = mutableStateOf(PlacesState(places = mapCurrentPlacesToUi(selectedPlace = null)))
    val state: State<PlacesState> get() = _state

    fun getSaved() {
        viewModelScope.launch {
            currentPlaces = getSavedPlaces()
            val placesUi = mapCurrentPlacesToUi(getSelectedPlace())
            _state.value = _state.value.copy(
                places = placesUi,
                empty = if (placesUi.isEmpty()) PlacesEmpty.NoSavedPlaces else null,
                isLoading = false
            )
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            currentPlaces = searchPlaces(query)
            val placesUi = mapCurrentPlacesToUi(getSelectedPlace())

            _state.value = _state.value.copy(
                places = placesUi,
                empty = if (placesUi.isEmpty()) PlacesEmpty.NoPlacesFoundForQuery else null,
                isLoading = false
            )
        }
    }

    fun select(index: Int) {
        viewModelScope.launch {
            selectPlace(currentPlaces[index])
        }
    }

    private fun mapCurrentPlacesToUi(selectedPlace: Place? = null): List<PlaceUi> =
        currentPlaces.map { mapToPlaceUi(it, isSelected = it == selectedPlace) }
}