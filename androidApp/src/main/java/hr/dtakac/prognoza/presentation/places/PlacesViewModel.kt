package hr.dtakac.prognoza.presentation.places

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.simpleEvent
import hr.dtakac.prognoza.presentation.WidgetRefresher
import hr.dtakac.prognoza.shared.domain.*
import hr.dtakac.prognoza.shared.entity.Place
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlaces: SearchPlaces,
    private val getSavedPlaces: GetSavedPlaces,
    private val selectPlace: SelectPlace,
    private val getSelectedPlace: GetSelectedPlace,
    private val deleteSavedPlace: DeleteSavedPlace,
    private val widgetRefresher: WidgetRefresher,
    private val mapper: PlacesUiMapper
) : ViewModel() {
    private var currentPlaces: List<Place> = listOf()

    private val _state = mutableStateOf(PlacesState())
    val state: State<PlacesState> get() = _state

    init {
        getSaved()
    }

    fun getSaved() {
        viewModelScope.launch {
            showLoader()
            showSaved()
            hideLoader()
        }
    }

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            showLoader()
            showSearchResults(query)
            hideLoader()
        }
    }

    fun selectPlace(index: Int) {
        viewModelScope.launch {
            showLoader()
            selectPlace(currentPlaces[index])
            showSaved().also {
                _state.value = _state.value.copy(placeSelected = simpleEvent())
            }
            widgetRefresher.refreshData()
            hideLoader()
        }
    }

    fun deletePlace(index: Int) {
        viewModelScope.launch {
            showLoader()
            deleteSavedPlace(currentPlaces[index])
            showSaved()
            hideLoader()
        }
    }

    private suspend fun showSaved() {
        val savedPlaces = getSavedPlaces()
        if (savedPlaces.isEmpty()) {
            _state.value = _state.value.copy(
                provider = null,
                empty = TextResource.fromStringId(R.string.no_saved_places)
            )
        } else {
            currentPlaces = savedPlaces
            _state.value = _state.value.copy(
                places = mapper.mapToSavedPlacesUi(savedPlaces, getSelectedPlace()),
                provider = null,
                empty = null
            )
        }
    }

    private suspend fun showSearchResults(query: String) {
        when (val result = searchPlaces(query)) {
            is SearchPlacesResult.Success -> {
                currentPlaces = result.places
                _state.value = _state.value.copy(
                    places = mapper.mapToSearchResultPlacesUi(result.places),
                    provider = mapper.getProvider(),
                    empty = null
                )
            }
            is SearchPlacesResult.Empty -> _state.value = _state.value.copy(
                empty = mapper.mapToSearchPlacesError(result, query),
                provider = null
            )
        }
    }

    private fun showLoader() {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun hideLoader() {
        _state.value = _state.value.copy(isLoading = false)
    }
}