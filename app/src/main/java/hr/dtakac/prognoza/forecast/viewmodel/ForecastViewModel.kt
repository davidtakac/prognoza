package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.database.entity.shortenedName
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentPlaceId: String? = null

    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    fun getPlaceName() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                val selectedPlace = placeRepository.get(preferencesRepository.getSelectedPlaceId())
                _placeName.value = selectedPlace.shortenedName
                currentPlaceId = selectedPlace.id
            }
        }
    }

    private suspend fun isReloadNeeded(): Boolean {
        return currentPlaceId != preferencesRepository.getSelectedPlaceId()
    }
}