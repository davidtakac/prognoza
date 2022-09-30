package hr.dtakac.prognoza.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.*
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.ActionTimedLatch
import hr.dtakac.prognoza.themesetting.ThemeSetting
import hr.dtakac.prognoza.themesetting.usecase.GetAllThemeSettings
import hr.dtakac.prognoza.themesetting.usecase.GetThemeSetting
import hr.dtakac.prognoza.themesetting.usecase.SetThemeSetting
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setTemperatureUnit: SetTemperatureUnit,
    private val getTemperatureUnit: GetTemperatureUnit,
    private val getAllTemperatureUnits: GetAllTemperatureUnits,
    private val setWindUnit: SetWindUnit,
    private val getWindUnit: GetWindUnit,
    private val getAllWindUnits: GetAllWindUnits,
    private val setPrecipitationUnit: SetPrecipitationUnit,
    private val getPrecipitationUnit: GetPrecipitationUnit,
    private val getAllPrecipitationUnits: GetAllPrecipitationUnits,
    private val setPressureUnit: SetPressureUnit,
    private val getPressureUnit: GetPressureUnit,
    private val getAllPressureUnits: GetAllPressureUnits,
    private val setThemeSetting: SetThemeSetting,
    private val getThemeSetting: GetThemeSetting,
    private val getAllThemeSettings: GetAllThemeSettings
) : ViewModel() {
    private val loaderTimedLatch = ActionTimedLatch(viewModelScope)

    private var availableTemperatureUnits: List<TemperatureUnit> = listOf()
    private var availableWindUnits: List<SpeedUnit> = listOf()
    private var availablePrecipitationUnits: List<LengthUnit> = listOf()
    private var availablePressureUnits: List<PressureUnit> = listOf()
    private var availableThemeSettings: List<ThemeSetting> = listOf()

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> get() = _state

    fun getState() {
        updateState { }
    }

    fun setTemperatureUnit(index: Int) {
        updateState {
            setTemperatureUnit(availableTemperatureUnits[index])
        }
    }

    fun setWindUnit(index: Int) {
        updateState {
            setWindUnit(availableWindUnits[index])
        }
    }

    fun setPrecipitationUnit(index: Int) {
        updateState {
            setPrecipitationUnit(availablePrecipitationUnits[index])
        }
    }

    fun setPressureUnit(index: Int) {
        updateState {
            setPressureUnit(availablePressureUnits[index])
        }
    }

    fun setTheme(index: Int) {
        updateState {
            setThemeSetting(availableThemeSettings[index])
        }
    }

    private fun updateState(action: suspend () -> Unit) {
        viewModelScope.launch {
            showLoader()
            action()
            getStateActual()
            hideLoader()
        }
    }

    private suspend fun getStateActual() {
        availableTemperatureUnits = getAllTemperatureUnits()
        availableWindUnits = getAllWindUnits()
        availablePrecipitationUnits = getAllPrecipitationUnits()
        availablePressureUnits = getAllPressureUnits()
        availableThemeSettings = getAllThemeSettings()

        _state.value = _state.value.copy(
            temperatureUnitSetting = mapToTemperatureUnitSetting(
                selectedTemperatureUnit = getTemperatureUnit(),
                availableTemperatureUnits = availableTemperatureUnits
            ),
            windUnitSetting = mapToWindUnitSetting(
                selectedWindUnit = getWindUnit(),
                availableWindUnits = availableWindUnits
            ),
            precipitationUnitSetting = mapToPrecipitationUnitSetting(
                selectedPrecipitationUnit = getPrecipitationUnit(),
                availablePrecipitationUnits = availablePrecipitationUnits
            ),
            pressureUnitSetting = mapToPressureUnitSetting(
                selectedPressureUnit = getPressureUnit(),
                availablePressureUnits = availablePressureUnits
            ),
            themeSetting = mapToThemeSetting(
                selectedThemeSetting = getThemeSetting(),
                availableThemeSettings = availableThemeSettings
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