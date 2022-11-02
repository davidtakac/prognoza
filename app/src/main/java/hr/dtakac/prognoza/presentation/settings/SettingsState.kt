package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.presentation.Event
import hr.dtakac.prognoza.presentation.TextResource

data class SettingsState(
    val isLoading: Boolean = false,
    val temperatureUnitSetting: MultipleChoiceSetting? = null,
    val windUnitSetting: MultipleChoiceSetting? = null,
    val precipitationUnitSetting: MultipleChoiceSetting? = null,
    val pressureUnitSetting: MultipleChoiceSetting? = null,
    val themeSetting: MultipleChoiceSetting? = null,
    val forecastCredit: DisplaySetting? = null,
    val geolocationCredit: DisplaySetting? = null,
    val designCredit: DisplaySetting? = null,
    val unitChanged: Event<Unit>? = null,
    val themeChanged: Event<Unit>? = null
)

data class MultipleChoiceSetting(
    val name: TextResource,
    val value: TextResource,
    val values: List<TextResource>,
    val onValuePick: (Int) -> Unit
)

data class DisplaySetting(
    val name: TextResource,
    val value: TextResource,
    val onClick: () -> Unit
)