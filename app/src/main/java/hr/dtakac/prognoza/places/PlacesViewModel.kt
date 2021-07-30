package hr.dtakac.prognoza.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.Place
import hr.dtakac.prognoza.database.entity.shortenedName
import hr.dtakac.prognoza.places.uimodel.PlaceUiModel
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val placeRepository: PlaceRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var displayedPlaces = listOf<Place>()
    private val _places = MutableLiveData<List<PlaceUiModel>>()
    val places: LiveData<List<PlaceUiModel>> get() = _places

    fun getSavedPlaces() {
        coroutineScope.launch {
            displayedPlaces = placeRepository.getSavedPlaces()
            _places.value = displayedPlaces.mapToPlaceUiModels()
        }
    }

    fun search(query: String) {
        coroutineScope.launch {
            displayedPlaces = placeRepository.search(query)
            _places.value = displayedPlaces.mapToPlaceUiModels()
        }
    }

    fun select(placeId: String) {
        coroutineScope.launch {
            val selectedPlace = withContext(dispatcherProvider.default) {
                displayedPlaces.firstOrNull { it.id == placeId }
                    ?: TODO("show that error has occurred")
            }
            placeRepository.selectPlace(selectedPlace)
        }
    }

    private suspend fun List<Place>.mapToPlaceUiModels() =
        withContext(dispatcherProvider.default) {
            map {
                PlaceUiModel(
                    id = it.id,
                    name = it.shortenedName,
                    fullName = it.fullName,
                    isSaved = it.isSaved
                )
            }
        }
}