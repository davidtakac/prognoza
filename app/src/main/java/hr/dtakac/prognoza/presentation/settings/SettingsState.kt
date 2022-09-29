package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.presentation.TextResource

data class SettingsState(
    val isLoading: Boolean = false,
    val temperatureUnitSetting: UnitSetting? = null,
    val windUnitSetting: UnitSetting? = null,
    val precipitationUnitSetting: UnitSetting? = null
)

data class UnitSetting(
    val name: TextResource,
    val value: TextResource,
    val values: List<TextResource>
)