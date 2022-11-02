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
import hr.dtakac.prognoza.presentation.theme.ThemeSetting
import hr.dtakac.prognoza.presentation.theme.ThemeSettingRepository
import hr.dtakac.prognoza.presentation.WidgetRefresher
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
    private val themeSettingRepository: ThemeSettingRepository,
    private val widgetRefresher: WidgetRefresher,
    private val mapper: SettingsUiMapper
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

    private fun setTemperatureUnit(index: Int) {
        updateState {
            setTemperatureUnit(availableTemperatureUnits[index])
            fireUnitChanged()
        }
    }

    private fun setWindUnit(index: Int) {
        updateState {
            setWindUnit(availableWindUnits[index])
            fireUnitChanged()
        }
    }

    private fun setPrecipitationUnit(index: Int) {
        updateState {
            setPrecipitationUnit(availablePrecipitationUnits[index])
            fireUnitChanged()
        }
    }

    private fun setPressureUnit(index: Int) {
        updateState {
            setPressureUnit(availablePressureUnits[index])
            fireUnitChanged()
        }
    }

    private fun setTheme(index: Int) {
        updateState {
            themeSettingRepository.setTheme(availableThemeSettings[index])
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
        availableThemeSettings = themeSettingRepository.getAvailableThemes()

        _state.value = _state.value.copy(
            temperatureUnitSetting = mapper.mapToTemperatureUnitSetting(
                selectedTemperatureUnit = getTemperatureUnit(),
                availableTemperatureUnits = availableTemperatureUnits,
                onValuePick = ::setTemperatureUnit
            ),
            windUnitSetting = mapper.mapToWindUnitSetting(
                selectedWindUnit = getWindUnit(),
                availableWindUnits = availableWindUnits,
                onValuePick = ::setWindUnit
            ),
            precipitationUnitSetting = mapper.mapToPrecipitationUnitSetting(
                selectedPrecipitationUnit = getPrecipitationUnit(),
                availablePrecipitationUnits = availablePrecipitationUnits,
                onValuePick = ::setPrecipitationUnit
            ),
            pressureUnitSetting = null,
            themeSetting = mapper.mapToThemeSetting(
                selectedThemeSetting = themeSettingRepository.getCurrentTheme(),
                availableThemeSettings = availableThemeSettings,
                onValuePick = ::setTheme
            ),
            forecastCredit = mapper.getForecastCreditDisplaySetting(
                onClick = { openLink("https://www.met.no/en") }
            ),
            geolocationCredit = mapper.getGeolocationCreditDisplaySetting(
                onClick = { openLink("https://www.openstreetmap.org") }
            ),
            designCredit = mapper.getDesignCreditDisplaySetting(
                onClick = { openLink("https://dribbble.com/shots/6680361-Dribbble-Daily-UI-37-Weather-2") }
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

    private fun openLink(url: String) {
        // todo
    }
}