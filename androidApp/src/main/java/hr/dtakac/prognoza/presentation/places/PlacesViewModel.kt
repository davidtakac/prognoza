package hr.dtakac.prognoza.presentation.places

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.presentation.simpleEvent
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
    private val mapper: PlacesUiMapper
) : ViewModel() {
    private var currentPlaces: List<Place> = listOf()
    var state by mutableStateOf(PlacesState())
        private set

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
            showSaved().also { state = state.copy(placeSelected = simpleEvent()) }
            hideLoader()
        }
    }

    fun deletePlace(index: Int) {
        viewModelScope.launch {
            showLoader()
            deleteSavedPlace(currentPlaces[index].id)
            showSaved()
            hideLoader()
        }
    }

    private suspend fun showSaved() {
        val savedPlaces = getSavedPlaces()
        if (savedPlaces.isEmpty()) {
            state = state.copy(
                provider = null,
                empty = mapper.getEmptyMessage()
            )
        } else {
            currentPlaces = savedPlaces
            state = state.copy(
                places = mapper.mapToSavedPlacesUi(savedPlaces, getSelectedPlace()),
                provider = null,
                empty = null
            )
        }
    }

    private suspend fun showSearchResults(query: String) {
        val places = searchPlaces(query)
        if (!places.isNullOrEmpty()) {
            currentPlaces = places
            state = state.copy(
                places = mapper.mapToSearchResultPlacesUi(places),
                provider = mapper.getProvider(),
                empty = null
            )
        } else {
            state = state.copy(
                empty = mapper.mapToSearchPlacesError(places, query),
                provider = null
            )
        }
    }

    private fun showLoader() {
        state = state.copy(isLoading = true)
    }

    private fun hideLoader() {
        state = state.copy(isLoading = false)
    }
}