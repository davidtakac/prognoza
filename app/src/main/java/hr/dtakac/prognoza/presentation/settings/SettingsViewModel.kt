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
import hr.dtakac.prognoza.presentation.simpleEvent
import hr.dtakac.prognoza.ui.ThemeSetting
import hr.dtakac.prognoza.ui.ThemeChanger
import hr.dtakac.prognoza.ui.WidgetRefresher
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
    private val themeChanger: ThemeChanger,
    private val widgetRefresher: WidgetRefresher
) : ViewModel() {
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
            fireUnitChanged()
        }
    }

    fun setWindUnit(index: Int) {
        updateState {
            setWindUnit(availableWindUnits[index])
            fireUnitChanged()
        }
    }

    fun setPrecipitationUnit(index: Int) {
        updateState {
            setPrecipitationUnit(availablePrecipitationUnits[index])
            fireUnitChanged()
        }
    }

    fun setPressureUnit(index: Int) {
        updateState {
            setPressureUnit(availablePressureUnits[index])
            fireUnitChanged()
        }
    }

    fun setTheme(index: Int) {
        updateState {
            themeChanger.setTheme(availableThemeSettings[index])
            fireThemeChanged()
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
        availableThemeSettings = themeChanger.getAvailableThemes()

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
            pressureUnitSetting = null,
            themeSetting = mapToThemeSetting(
                selectedThemeSetting = themeChanger.getCurrentTheme(),
                availableThemeSettings = availableThemeSettings
            )
        )
    }

    private fun showLoader() {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun hideLoader() {
        _state.value = _state.value.copy(isLoading = false)
    }

    private fun fireUnitChanged() {
        widgetRefresher.refresh()
        _state.value = _state.value.copy(
            unitChanged = simpleEvent()
        )
    }

    private fun fireThemeChanged() {
        _state.value = _state.value.copy(
            themeChanged = simpleEvent()
        )
    }
}