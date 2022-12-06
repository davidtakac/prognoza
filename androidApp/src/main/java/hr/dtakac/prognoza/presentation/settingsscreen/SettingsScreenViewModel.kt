package hr.dtakac.prognoza.presentation.settingsscreen

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.presentation.Event
import hr.dtakac.prognoza.presentation.simpleEvent
import hr.dtakac.prognoza.androidsettings.model.ThemeSetting
import hr.dtakac.prognoza.presentation.WidgetRefresher
import hr.dtakac.prognoza.androidsettings.model.MoodMode
import hr.dtakac.prognoza.androidsettings.AndroidSettingsRepository
import hr.dtakac.prognoza.shared.domain.*
import hr.dtakac.prognoza.shared.entity.LengthUnit
import hr.dtakac.prognoza.shared.entity.PressureUnit
import hr.dtakac.prognoza.shared.entity.SpeedUnit
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
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
    private val androidSettingsRepository: AndroidSettingsRepository,
    private val widgetRefresher: WidgetRefresher,
    private val mapper: SettingsScreenUiMapper
) : ViewModel() {
    private var availableTemperatureUnits: List<TemperatureUnit> = listOf()
    private var availableWindUnits: List<SpeedUnit> = listOf()
    private var availablePrecipitationUnits: List<LengthUnit> = listOf()
    private var availablePressureUnits: List<PressureUnit> = listOf()
    private var availableThemeSettings: List<ThemeSetting> = listOf()
    private var availableMoodModes: List<MoodMode> = listOf()

    private val _state = mutableStateOf(SettingsScreenState())
    val state: State<SettingsScreenState> get() = _state

    fun getState() {
        updateState()
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
            androidSettingsRepository.setTheme(availableThemeSettings[index])
            fireThemeChanged()
        }
    }

    private fun setMoodMode(index: Int) {
        updateState {
            androidSettingsRepository.setMoodMode(availableMoodModes[index])
            fireMoodModeChanged()
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
        availableTemperatureUnits = getAllTemperatureUnits()
        availableWindUnits = getAllWindUnits()
        availablePrecipitationUnits = getAllPrecipitationUnits()
        availablePressureUnits = getAllPressureUnits()
        availableThemeSettings = androidSettingsRepository.getAvailableThemes()
        availableMoodModes = androidSettingsRepository.getAvailableMoodModes()

        _state.value = _state.value.copy(
            unitSettings = listOf(
                mapper.mapToTemperatureUnitSetting(
                    selected = getTemperatureUnit(),
                    units = availableTemperatureUnits,
                    onIndexSelected = ::setTemperatureUnit
                ),
                mapper.mapToWindUnitSetting(
                    selected = getWindUnit(),
                    units = availableWindUnits,
                    onIndexSelected = ::setWindUnit
                ),
                mapper.mapToPrecipitationUnitSetting(
                    selected = getPrecipitationUnit(),
                    units = availablePrecipitationUnits,
                    onIndexSelected = ::setPrecipitationUnit
                )
            ),
            appearanceSettings = mutableListOf<MultipleChoiceSettingUi>().apply {
                add(mapper.mapToThemeSetting(
                    selected = androidSettingsRepository.getTheme(),
                    options = availableThemeSettings,
                    onIndexSelected = ::setTheme
                ))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    add(mapper.mapToMoodModeSetting(
                        selected = androidSettingsRepository.getMoodMode(),
                        options = availableMoodModes,
                        onIndexSelected = ::setMoodMode
                    ))
                }
            },
            creditSettings = listOf(
                mapper.getForecastCreditDisplaySetting(
                    onClick = { openLink("https://www.met.no/en") }
                ),
                mapper.getGeolocationCreditDisplaySetting(
                    onClick = { openLink("https://www.openstreetmap.org") }
                ),
                mapper.getDesignCreditDisplaySetting(
                    onClick = { openLink("https://dribbble.com/shots/6680361-Dribbble-Daily-UI-37-Weather-2") }
                ),
                mapper.getIconCreditDisplaySetting(
                    onClick = { openLink("https://www.instagram.com/art.ofil/") }
                )
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
        widgetRefresher.refreshData()
        _state.value = _state.value.copy(
            unitChangedEvent = simpleEvent()
        )
    }

    private fun fireThemeChanged() {
        _state.value = _state.value.copy(
            themeChangedEvent = simpleEvent()
        )
    }

    private fun fireMoodModeChanged() {
        _state.value = _state.value.copy(
            moodModeChangedEvent = simpleEvent()
        )
    }

    private fun openLink(url: String) {
        _state.value = _state.value.copy(
            openLinkEvent = Event(url)
        )
    }
}