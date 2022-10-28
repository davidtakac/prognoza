package hr.dtakac.prognoza.presentation.places

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.domain.usecase.*
import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.simpleEvent
import hr.dtakac.prognoza.presentation.WidgetRefresher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlaces: SearchPlaces,
    private val getSavedPlaces: GetSavedPlaces,
    private val selectPlace: SelectPlace,
    private val getSelectedPlace: GetSelectedPlace,
    private val widgetRefresher: WidgetRefresher,
    private val mapper: PlacesUiMapper
) : ViewModel() {
    private var currentPlaces: List<Place> = listOf()

    private val _state = mutableStateOf(PlacesState())
    val state: State<PlacesState> get() = _state

    fun getSaved() {
        viewModelScope.launch {
            showLoader()
            val savedPlaces = getSavedPlaces()
            if (savedPlaces.isEmpty()) {
                _state.value = _state.value.copy(
                    empty = TextResource.fromStringId(R.string.no_saved_places)
                )
            } else {
                currentPlaces = savedPlaces
                _state.value = _state.value.copy(
                    places = mapper.mapToPlaceUi(savedPlaces, getSelectedPlace()),
                    empty = null
                )
            }
            hideLoader()
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            showLoader()
            when (val searchPlacesResult = searchPlaces(query)) {
                is SearchPlacesResult.Success -> {
                    currentPlaces = searchPlacesResult.places
                    _state.value = _state.value.copy(
                        places = mapper.mapToPlaceUi(searchPlacesResult.places, getSelectedPlace()),
                        empty = null
                    )
                }
                is SearchPlacesResult.Empty -> _state.value = _state.value.copy(
                    empty = mapper.mapToSearchPlacesError(searchPlacesResult, query)
                )
            }
            hideLoader()
        }
    }

    fun select(index: Int) {
        viewModelScope.launch {
            showLoader()
            val selectedPlace = currentPlaces[index]
            selectPlace(selectedPlace)
            _state.value = _state.value.copy(
                places = mapper.mapToPlaceUi(currentPlaces, selectedPlace),
                placeSelected = simpleEvent()
            )
            widgetRefresher.refresh()
            hideLoader()
        }
    }

    private fun showLoader() {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun hideLoader() {
        _state.value = _state.value.copy(isLoading = false)
    }
}