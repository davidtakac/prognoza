package hr.dtakac.prognoza.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.common.toPlaceUiModels
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

    fun showSavedPlaces() {
        coroutineScope.launch {
            _isLoading.value = true
            displayedPlaces = placeRepository.getAll()
            _places.value = withContext(dispatcherProvider.default) {
                displayedPlaces.toPlaceUiModels()
            }
            _isLoading.value = false
        }
    }

    fun search(query: String) {
        coroutineScope.launch {
            _isLoading.value = true
            displayedPlaces = placeRepository.search(query)
            _places.value = withContext(dispatcherProvider.default) {
                displayedPlaces.toPlaceUiModels()
            }
            _isLoading.value = false
        }
    }

    fun select(placeId: String) {
        coroutineScope.launch {
            _isLoading.value = true
            val selectedPlace = withContext(dispatcherProvider.default) {
                displayedPlaces.firstOrNull { it.id == placeId }
                    ?: TODO("show that error has occurred")
            }
            placeRepository.save(selectedPlace)
            preferencesRepository.setSelectedPlaceId(selectedPlace.id)
            _isLoading.value = false
            _placeSelectedEvent.value = Event(true)
        }
    }
}