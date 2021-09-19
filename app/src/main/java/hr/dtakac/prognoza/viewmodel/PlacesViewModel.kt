package hr.dtakac.prognoza.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.Event
import hr.dtakac.prognoza.common.network.NetworkChecker
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.dbmodel.Place
import hr.dtakac.prognoza.utils.toPlaceUiModel
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.cell.PlaceUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository,
    private val networkChecker: NetworkChecker
) : CoroutineScopeViewModel(coroutineScope) {
    private var displayedPlaces = listOf<Place>()

    private val _places = MutableLiveData<List<PlaceUiModel>>()
    val places: LiveData<List<PlaceUiModel>> get() = _places

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _placeSelectedEvent = MutableLiveData<Event<Unit>>()
    val placeSelectedEvent: LiveData<Event<Unit>> get() = _placeSelectedEvent

    private val _message = MutableLiveData<Event<Int>>()
    val message: LiveData<Event<Int>> get() = _message

    fun showPlaces(query: String = "") {
        when {
            query.isBlank() -> {
                showSavedPlaces()
            }
            networkChecker.hasInternetConnection() -> {
                search(query)
            }
            else -> {
                _message.value = Event(R.string.notify_no_internet)
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
                displayedPlaces.firstOrNull { it.id == placeId }
                    ?: placeRepository.getDefaultPlace()
            }
            placeRepository.save(selectedPlace)
            preferencesRepository.setSelectedPlaceId(selectedPlace.id)
            _isLoading.value = false
            _placeSelectedEvent.value = Event(Unit)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
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
}