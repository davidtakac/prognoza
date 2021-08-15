package hr.dtakac.prognoza.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.common.util.shortenedName
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastActivityViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentPlaceId: String? = null

    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    fun getPlaceName() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                val selectedPlace = placeRepository.get(preferencesRepository.getSelectedPlaceId())
                    ?: placeRepository.getDefaultPlace()
                _placeName.value = selectedPlace.shortenedName
                currentPlaceId = selectedPlace.id
            }
        }
    }

    fun cleanUpDatabase() {
        coroutineScope.launch {
            forecastRepository.deleteExpiredData()
        }
    }

    private suspend fun isReloadNeeded(): Boolean {
        return currentPlaceId != preferencesRepository.getSelectedPlaceId()
    }
}