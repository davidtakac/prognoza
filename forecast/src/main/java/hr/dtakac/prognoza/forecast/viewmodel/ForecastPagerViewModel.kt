package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import hr.dtakac.prognoza.core.model.ui.MeasurementUnit
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.utils.shortenedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastPagerViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository
) : hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel(coroutineScope) {
    private var currentPlaceId: String? = null
    private var currentUnit: MeasurementUnit? = null

    private val _placeName = mutableStateOf("")
    val placeName: State<String> get() = _placeName

    private val _preferredUnit = mutableStateOf(MeasurementUnit.METRIC)
    val preferredUnit: State<MeasurementUnit> get() = _preferredUnit

    init {
        getData()
    }

    fun getData() {
        coroutineScope.launch {
            getSelectedUnitActual()
            getSelectedPlaceNameActual()
        }
    }

    private suspend fun getSelectedUnitActual() {
        if (isUnitReloadNeeded()) {
            val selectedUnit = preferencesRepository.getSelectedUnit()
            _preferredUnit.value = selectedUnit
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