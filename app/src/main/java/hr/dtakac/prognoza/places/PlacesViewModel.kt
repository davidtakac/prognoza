package hr.dtakac.prognoza.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.common.DEFAULT_PLACE_ID
import hr.dtakac.prognoza.common.shortenedName
import hr.dtakac.prognoza.common.toPlaceUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var displayedPlaces = listOf<Place>()

    private val _places = MutableLiveData<List<PlaceUiModel>>()
    val places: LiveData<List<PlaceUiModel>> get() = _places

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _placeSelectedEvent = MutableLiveData<Event<Boolean>>()
    val placeSelectedEvent: LiveData<Event<Boolean>> get() = _placeSelectedEvent

    private val _placeDeletedEvent = MutableLiveData<Event<String>>()
    val placeDeletedEvent: LiveData<Event<String>> get() = _placeDeletedEvent

    fun showSavedPlaces() {
        coroutineScope.launch {
            _isLoading.value = true
            setDisplayedPlaces(placeRepository.getAll())
            _isLoading.value = false
        }
    }

    fun search(query: String) {
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
                displayedPlaces.firstOrNull { it.id == placeId }
                    ?: placeRepository.get(DEFAULT_PLACE_ID)
            }
            placeRepository.save(selectedPlace)
            preferencesRepository.setSelectedPlaceId(selectedPlace.id)
            _isLoading.value = false
            _placeSelectedEvent.value = Event(true)
        }
    }

    fun delete(placeId: String) {
        coroutineScope.launch {
            val placeToDelete = withContext(dispatcherProvider.default) {
                displayedPlaces.firstOrNull { it.id == placeId }
            }
            if (placeToDelete != null) {
                placeRepository.delete(placeToDelete)
                _placeDeletedEvent.value = Event(placeToDelete.shortenedName)
            }
        }
    }

    private suspend fun setDisplayedPlaces(places: List<Place>) {
        displayedPlaces = places
        _places.value = withContext(dispatcherProvider.default) {
            displayedPlaces.map { it.toPlaceUiModel() }
        }
    }
}