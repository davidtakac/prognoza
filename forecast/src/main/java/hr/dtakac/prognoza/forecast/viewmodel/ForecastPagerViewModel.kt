package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.utils.shortenedName
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastPagerViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository
) : hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel(coroutineScope) {
    private var currentPlaceId: String? = null
    private var currentUnit: MeasurementUnit? = null

    private val _placeName = mutableStateOf("")
    val placeName: State<String> get() = _placeName

    private val _selectedUnit = mutableStateOf(MeasurementUnit.METRIC)
    val selectedUnit: State<MeasurementUnit> get() = _selectedUnit

    init {
        getSelectedUnit()
        getSelectedPlaceName()
    }

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
        }
    }

    fun changeSelectedPlace() {
        coroutineScope.launch {
            getSelectedPlaceNameActual()
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
        return currentPlaceId != preferencesRepository.getSelectedPlaceId()
    }

    private suspend fun isUnitReloadNeeded(): Boolean {
        return currentUnit != preferencesRepository.getSelectedUnit()
    }
}