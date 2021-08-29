package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.Event
import hr.dtakac.prognoza.extensions.shortenedName
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastActivityViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentPlaceId: String? = null
    private var currentUnit: MeasurementUnit? = null

    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    private val _selectedUnit = MutableLiveData<MeasurementUnit>()
    val selectedUnit: LiveData<MeasurementUnit> get() = _selectedUnit

    private val _placeChangedEvent = MutableLiveData<Event<Unit>>()
    val placeChangedEvent: LiveData<Event<Unit>> get() = _placeChangedEvent

    private val _unitChangedEvent = MutableLiveData<Event<Unit>>()
    val unitChangedEvent: LiveData<Event<Unit>> get() = _unitChangedEvent

    fun getSelectedUnit() {
        coroutineScope.launch {
            if (isUnitsReloadNeeded()) {
                _selectedUnit.value = preferencesRepository.getSelectedUnit()
            }
        }
    }

    fun getSelectedPlaceName() {
        coroutineScope.launch {
            if (isPlaceReloadNeeded()) {
                val selectedPlace = placeRepository.get(preferencesRepository.getSelectedPlaceId())
                    ?: placeRepository.getDefaultPlace()
                _placeName.value = selectedPlace.shortenedName
                _placeChangedEvent.value = Event(Unit)
                currentPlaceId = selectedPlace.id
            }
        }
    }

    fun cleanUpDatabase() {
        coroutineScope.launch {
            forecastRepository.deleteExpiredData()
        }
    }

    fun changeSelectedUnit() {
        coroutineScope.launch {
            val newUnit = when (preferencesRepository.getSelectedUnit()) {
                MeasurementUnit.IMPERIAL -> MeasurementUnit.METRIC
                MeasurementUnit.METRIC -> MeasurementUnit.IMPERIAL
            }
            preferencesRepository.setSelectedUnit(newUnit)
            _selectedUnit.value = newUnit
            _unitChangedEvent.value = Event(Unit)
        }
    }

    private suspend fun isPlaceReloadNeeded(): Boolean {
        return currentPlaceId != preferencesRepository.getSelectedPlaceId() ||
                _placeName.value == null
    }

    private suspend fun isUnitsReloadNeeded(): Boolean {
        return currentUnit != preferencesRepository.getSelectedUnit() ||
                _selectedUnit.value == null
    }
}