package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import hr.dtakac.prognoza.core.model.database.Place
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

    private var currentPlace: Place? = null
    private var currentUnit: MeasurementUnit? = null

    private val _placeName = mutableStateOf<String?>(null)
    val placeName: State<String?> get() = _placeName

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
        val selectedUnit = preferencesRepository.getSelectedUnit()
        if (isUnitReloadNeeded(newUnit = selectedUnit)) {
            _preferredUnit.value = selectedUnit
            currentUnit = selectedUnit
        }
    }

    private suspend fun getSelectedPlaceNameActual() {
        val selectedPlace = preferencesRepository.getSelectedPlaceId()?.let {
            placeRepository.get(placeId = it)
        }
        if (isPlaceReloadNeeded(newPlace = selectedPlace)) {
            _placeName.value = selectedPlace?.shortenedName
            currentPlace = selectedPlace
        }
    }

    private fun isUnitReloadNeeded(newUnit: MeasurementUnit): Boolean {
        return currentUnit != newUnit
    }

    private fun isPlaceReloadNeeded(newPlace: Place?): Boolean {
        return currentPlace != newPlace
    }
}