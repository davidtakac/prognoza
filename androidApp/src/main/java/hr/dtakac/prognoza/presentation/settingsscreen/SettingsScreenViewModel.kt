package hr.dtakac.prognoza.presentation.settingsscreen

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.presentation.simpleEvent
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.AndroidSettingsRepository
import hr.dtakac.prognoza.shared.domain.*
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val selectTemperatureUnit: SelectTemperatureUnit,
    private val getSelectedTemperatureUnit: GetSelectedTemperatureUnit,
    private val getAvailableTemperatureUnits: GetAvailableTemperatureUnits,
    private val selectWindSpeedUnit: SelectWindSpeedUnit,
    private val getSelectedWindSpeedUnit: GetSelectedWindSpeedUnit,
    private val getAvailableWindSpeedUnits: GetAvailableWindSpeedUnits,
    private val selectLengthUnit: SelectLengthUnit,
    private val getSelectedLengthUnit: GetSelectedLengthUnit,
    private val getAvailableLengthUnits: GetAvailableLengthUnits,
    private val selectPressureUnit: SelectPressureUnit,
    private val getSelectedPressureUnit: GetSelectedPressureUnit,
    private val getAvailablePressureUnits: GetAvailablePressureUnits,
    private val androidSettingsRepository: AndroidSettingsRepository,
    private val mapper: SettingsScreenUiMapper
) : ViewModel() {
    private var availableTemperatureUnits: List<TemperatureUnit> = listOf()
    private var availableWindUnits: List<SpeedUnit> = listOf()
    private var availablePrecipitationUnits: List<LengthUnit> = listOf()
    private var availablePressureUnits: List<PressureUnit> = listOf()
    private var availableUiModes: List<UiMode> = listOf()
    private var availableMoodModes: List<MoodMode> = listOf()

    private val _state = mutableStateOf(SettingsScreenState())
    val state: State<SettingsScreenState> get() = _state

    init {
        updateState()
    }

    private fun setTemperatureUnit(index: Int) {
        updateState {
            selectTemperatureUnit(availableTemperatureUnits[index])
            fireUpdateForecast()
        }
    }

    private fun setWindUnit(index: Int) {
        updateState {
            selectWindSpeedUnit(availableWindUnits[index])
            fireUpdateForecast()
        }
    }

    private fun setPrecipitationUnit(index: Int) {
        updateState {
            selectLengthUnit(availablePrecipitationUnits[index])
            fireUpdateForecast()
        }
    }

    private fun setPressureUnit(index: Int) {
        updateState {
            selectPressureUnit(availablePressureUnits[index])
            fireUpdateForecast()
        }
    }

    private fun setTheme(index: Int) {
        updateState {
            androidSettingsRepository.setUiMode(availableUiModes[index])
            fireUpdateTheme()
        }
    }

    private fun setMoodMode(index: Int) {
        updateState {
            androidSettingsRepository.setMoodMode(availableMoodModes[index])
            fireUpdateTheme()
        }
    }

    private fun updateState(action: suspend () -> Unit = {}) {
        viewModelScope.launch {
            showLoader()
            action()
            getStateActual()
            hideLoader()
        }
    }

    private suspend fun getStateActual() {
        availableTemperatureUnits = getAvailableTemperatureUnits()
        availableWindUnits = getAvailableWindSpeedUnits()
        availablePrecipitationUnits = getAvailableLengthUnits()
        availablePressureUnits = getAvailablePressureUnits()
        availableUiModes = androidSettingsRepository.getAvailableUiModes()
        availableMoodModes = androidSettingsRepository.getAvailableMoodModes()

        _state.value = _state.value.copy(
            unitSettings = listOf(
                mapper.mapToTemperatureUnitSetting(
                    selected = getSelectedTemperatureUnit(),
                    units = availableTemperatureUnits,
                    onIndexSelected = ::setTemperatureUnit
                ),
                mapper.mapToWindUnitSetting(
                    selected = getSelectedWindSpeedUnit(),
                    units = availableWindUnits,
                    onIndexSelected = ::setWindUnit
                ),
                mapper.mapToPrecipitationUnitSetting(
                    selected = getSelectedLengthUnit(),
                    units = availablePrecipitationUnits,
                    onIndexSelected = ::setPrecipitationUnit
                )
            ),
            appearanceSettings = mutableListOf<MultipleChoiceSettingUi>().apply {
                add(mapper.mapToUiModeSetting(
                    selected = androidSettingsRepository.getUiMode(),
                    options = availableUiModes,
                    onIndexSelected = ::setTheme
                ))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    add(mapper.mapToMoodModeSetting(
                        selected = androidSettingsRepository.getMoodMode(),
                        options = availableMoodModes,
                        onIndexSelected = ::setMoodMode
                    ))
                }
            }
        )
    }

    private fun showLoader() {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun hideLoader() {
        _state.value = _state.value.copy(isLoading = false)
    }

    private fun fireUpdateForecast() {
        _state.value = _state.value.copy(
            updateForecastEvent = simpleEvent()
        )
    }

    private fun fireUpdateTheme() {
        _state.value = _state.value.copy(
            updateThemeEvent = simpleEvent()
        )
    }
}