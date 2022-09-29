package hr.dtakac.prognoza.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.*
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.ActionTimedLatch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setTemperatureUnit: SetTemperatureUnit,
    private val getTemperatureUnit: GetTemperatureUnit,
    private val getAllTemperatureUnits: GetAllTemperatureUnits,
    private val setWindUnit: SetWindUnit,
    private val getWindUnit: GetWindUnit,
    private val getAllWindUnits: GetAllWindUnits
) : ViewModel() {
    private val loaderTimedLatch = ActionTimedLatch(viewModelScope)

    private var availableTemperatureUnits: List<TemperatureUnit> = listOf()
    private var availableWindUnits: List<SpeedUnit> = listOf()

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> get() = _state

    fun getState() {
        viewModelScope.launch {
            showLoader()
            getStateActual()
            hideLoader()
        }
    }

    fun setTemperatureUnit(index: Int) {
        viewModelScope.launch {
            showLoader()
            val selected = availableTemperatureUnits[index]
            setTemperatureUnit(selected)
            getStateActual()
            hideLoader()
        }
    }

    fun setWindUnit(index: Int) {
        viewModelScope.launch {
            showLoader()
            val selected = availableWindUnits[index]
            setWindUnit(selected)
            getStateActual()
            hideLoader()
        }
    }

    private suspend fun getStateActual() {
        availableTemperatureUnits = getAllTemperatureUnits()
        availableWindUnits = getAllWindUnits()
        _state.value = _state.value.copy(
            temperatureUnitSetting = mapToTemperatureUnitSetting(
                selectedTemperatureUnit = getTemperatureUnit(),
                availableTemperatureUnits = availableTemperatureUnits
            ),
            windUnitSetting = mapToWindUnitSetting(
                selectedWindUnit = getWindUnit(),
                availableWindUnits = availableWindUnits
            )
        )
    }

    private fun showLoader() = loaderTimedLatch.start {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun hideLoader() = loaderTimedLatch.stop {
        _state.value = _state.value.copy(isLoading = false)
    }
}