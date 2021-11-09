package hr.dtakac.prognoza.places.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.network.NetworkChecker
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
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
    private val preferencesRepository: PreferencesRepository,
    private val networkChecker: NetworkChecker
) : hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel(coroutineScope) {
    private var displayedPlaces = listOf<Place>()

    private var _places = mutableStateOf<List<PlaceUiModel>>(listOf())
    val places: State<List<PlaceUiModel>> get() = _places

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _message = mutableStateOf<PlacesMessageUiModel?>(null)
    val message: State<PlacesMessageUiModel?> get() = _message

    private val _closePlaces = mutableStateOf(false)
    val closePlaces: State<Boolean> get() = _closePlaces

    init {
        showPlaces()
    }

    fun showPlaces(query: String = "") {
        when {
            query.isBlank() -> {
                showSavedPlaces()
            }
            networkChecker.hasInternetConnection() -> {
                search(query)
            }
            else -> {
                showNoConnectionMessage()
            }
        }
    }

    private fun showSavedPlaces() {
        coroutineScope.launch {
            _isLoading.value = true
            setDisplayedPlaces(placeRepository.getAll())
            _isLoading.value = false
        }
    }

    private fun search(query: String) {
        coroutineScope.launch {
            _isLoading.value = true
            setDisplayedPlaces(placeRepository.search(query))
            _isLoading.value = false
        }
    }

    fun select(placeId: String) {
        coroutineScope.launch {
            _isLoading.value = true
            val selectedPlace = withContext(dispatcherProvider.default) {
                displayedPlaces.first { it.id == placeId }
            }
            placeRepository.save(selectedPlace)
            preferencesRepository.setSelectedPlaceId(selectedPlace.id)
            _isLoading.value = false
            _closePlaces.value = true
        }
    }

    private suspend fun setDisplayedPlaces(places: List<Place>) {
        displayedPlaces = places
        _places.value = withContext(dispatcherProvider.default) {
            displayedPlaces.map {
                it.toPlaceUiModel(
                    isSaved = placeRepository.isSaved(it.id),
                    isSelected = preferencesRepository.getSelectedPlaceId() == it.id
                )
            }
        }
    }

    private fun showNoConnectionMessage() {
        coroutineScope.launch {
            _message.value = PlacesMessageUiModel(R.string.notify_no_internet)
            delay(3000)
            _message.value = null
        }
    }
}