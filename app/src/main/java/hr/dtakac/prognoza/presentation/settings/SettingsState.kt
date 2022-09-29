package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.presentation.TextResource

data class SettingsState(
    val isLoading: Boolean = false,
    val temperatureUnitSetting: MultipleChoiceSetting? = null,
    val windUnitSetting: MultipleChoiceSetting? = null,
    val precipitationUnitSetting: MultipleChoiceSetting? = null,
    val pressureUnitSetting: MultipleChoiceSetting? = null,
    val themeSetting: MultipleChoiceSetting? = null
)

data class MultipleChoiceSetting(
    val name: TextResource,
    val value: TextResource,
    val values: List<TextResource>
)