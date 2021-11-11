package hr.dtakac.prognoza.places.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.network.NetworkChecker
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.utils.ProgressTimeLatch
import hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel
import hr.dtakac.prognoza.places.R
import hr.dtakac.prognoza.places.mapping.toPlaceUiModel
import hr.dtakac.prognoza.places.model.EmptyPlacesUiModel
import hr.dtakac.prognoza.places.model.PlaceUiModel
import hr.dtakac.prognoza.places.model.PlacesMessageUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val placeRepository: PlaceRepository,
    private val networkChecker: NetworkChecker
) : CoroutineScopeViewModel(coroutineScope) {
    private var displayedPlaces = listOf<Place>()

    private var _places = mutableStateOf<List<PlaceUiModel>>(listOf())
    val places: State<List<PlaceUiModel>> get() = _places

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _message = mutableStateOf<PlacesMessageUiModel?>(null)
    val message: State<PlacesMessageUiModel?> get() = _message

    private val _closePlaces = mutableStateOf(false)
    val closePlaces: State<Boolean> get() = _closePlaces

    private val _emptyPlaces = mutableStateOf<EmptyPlacesUiModel?>(null)
    val emptyPlaces: State<EmptyPlacesUiModel?> get() = _emptyPlaces

    private val _selectedPlaces = mutableStateListOf<PlaceUiModel>()
    val selectedPlaces: SnapshotStateList<PlaceUiModel> get() = _selectedPlaces

    private val progressTimeLatch = ProgressTimeLatch {
        _isLoading.value = it
    }

    init { showSavedPlaces() }

    fun showSavedPlaces() {
        coroutineScope.launch {
            progressTimeLatch.loading = true
            showSavedPlacesActual()
            progressTimeLatch.loading = false
        }
    }

    fun search(query: String) {
        coroutineScope.launch(dispatcherProvider.default) {
            if (networkChecker.hasInternetConnection()) {
                progressTimeLatch.loading = true
                _selectedPlaces.clear()
                searchPlacesActual(query)
                progressTimeLatch.loading = false
            } else {
                _message.value = PlacesMessageUiModel(R.string.notify_no_internet)
                delay(3000)
                _message.value = null
            }
        }
    }

    fun handlePlaceClicked(place: PlaceUiModel) {
        coroutineScope.launch(dispatcherProvider.default) {
            progressTimeLatch.loading = true
            if (_selectedPlaces.isEmpty()) {
                pickAPlaceActual(place)
            } else {
                handlePlaceSelectedActual(place)
            }
            progressTimeLatch.loading = false
        }
    }

    fun handlePlaceSelected(place: PlaceUiModel) {
        coroutineScope.launch(dispatcherProvider.default) {
            handlePlaceSelectedActual(place)
        }
    }

    fun deletePlaces(places: List<PlaceUiModel>) {
        coroutineScope.launch {
            progressTimeLatch.loading = true
            placeRepository.deleteAll(places.map { it.id })
            showSavedPlacesActual()
            _selectedPlaces.removeAll(places)
            progressTimeLatch.loading = false
        }
    }

    fun clearPlaceSelection() {
        coroutineScope.launch(dispatcherProvider.default) {
            _selectedPlaces.clear()
        }
    }

    private suspend fun setDisplayedPlaces(places: List<Place>) {
        displayedPlaces = places
        _places.value = withContext(dispatcherProvider.default) {
            displayedPlaces.map {
                it.toPlaceUiModel(
                    isSaved = placeRepository.isSaved(it.id),
                    isPicked = placeRepository.isPicked(it.id)
                )
            }
        }
    }

    private suspend fun showSavedPlacesActual() {
        val places = placeRepository.getAll()
        setDisplayedPlaces(places)
        if (places.isEmpty()) {
            _emptyPlaces.value = EmptyPlacesUiModel(reason = R.string.no_saved_places)
        } else {
            _emptyPlaces.value = null
        }
    }

    private suspend fun searchPlacesActual(query: String) {
        val places = placeRepository.search(query)
        setDisplayedPlaces(places)
        if (places.isEmpty()) {
            _emptyPlaces.value = EmptyPlacesUiModel(reason = R.string.no_places_for_query)
        } else {
            _emptyPlaces.value = null
        }
    }

    private suspend fun pickAPlaceActual(place: PlaceUiModel) {
        val selectedPlace = withContext(dispatcherProvider.default) {
            displayedPlaces.first { it.id == place.id }
        }
        placeRepository.pick(selectedPlace)
        _closePlaces.value = true
    }

    private suspend fun handlePlaceSelectedActual(place: PlaceUiModel) {
        withContext(dispatcherProvider.default) {
            if (_places.value.all { it.isSaved }) {
                if (place in _selectedPlaces) {
                    _selectedPlaces.remove(place)
                } else {
                    _selectedPlaces.add(place)
                }
            }
        }
    }
}