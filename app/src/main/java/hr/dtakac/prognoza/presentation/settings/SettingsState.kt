package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.presentation.TextResource

data class SettingsState(
    val isLoading: Boolean = false,
    val temperatureUnitSetting: TemperatureUnitSetting? = null
)

data class TemperatureUnitSetting(
    val name: TextResource,
    val value: TextResource,
    val values: List<TextResource>
)