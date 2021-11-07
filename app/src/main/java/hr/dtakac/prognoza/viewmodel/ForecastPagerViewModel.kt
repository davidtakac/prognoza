package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.Event
import hr.dtakac.prognoza.common.utils.shortenedName
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastPagerViewModel(
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
            getSelectedUnitActual()
        }
    }

    fun getSelectedPlaceName() {
        coroutineScope.launch {
            getSelectedPlaceNameActual()
        }
    }

    fun changeSelectedUnit() {
        coroutineScope.launch {
            changeSelectedUnitActual()
            getSelectedUnitActual()
            _unitChangedEvent.value = Event(Unit)
        }
    }

    fun changeSelectedPlace() {
        coroutineScope.launch {
            getSelectedPlaceNameActual()
            _placeChangedEvent.value = Event(Unit)
        }
    }

    fun cleanUpDatabase() {
        coroutineScope.launch {
            forecastRepository.deleteExpiredData()
        }
    }

    private suspend fun changeSelectedUnitActual() {
        val newUnit = when (preferencesRepository.getSelectedUnit()) {
            MeasurementUnit.IMPERIAL -> MeasurementUnit.METRIC
            MeasurementUnit.METRIC -> MeasurementUnit.IMPERIAL
        }
        preferencesRepository.setSelectedUnit(newUnit)
    }

    private suspend fun getSelectedUnitActual() {
        if (isUnitReloadNeeded()) {
            val selectedUnit = preferencesRepository.getSelectedUnit()
            _selectedUnit.value = selectedUnit
            currentUnit = selectedUnit
        }
    }

    private suspend fun getSelectedPlaceNameActual() {
        if (isPlaceReloadNeeded()) {
            val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
            val selectedPlace = placeRepository.get(selectedPlaceId)
                ?: placeRepository.getDefaultPlace()
            _placeName.value = selectedPlace.shortenedName
            currentPlaceId = selectedPlace.id
        }
    }

    private suspend fun isPlaceReloadNeeded(): Boolean {
        return currentPlaceId != preferencesRepository.getSelectedPlaceId() ||
                _placeName.value == null
    }

    private suspend fun isUnitReloadNeeded(): Boolean {
        return currentUnit != preferencesRepository.getSelectedUnit() ||
                _selectedUnit.value == null
    }
}