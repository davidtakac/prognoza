package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.presentation.Event
import hr.dtakac.prognoza.presentation.TextResource

data class SettingsState(
    val isLoading: Boolean = false,
    val unitSettings: List<MultipleChoiceSetting> = listOf(),
    val appearanceSettings: List<MultipleChoiceSetting> = listOf(),
    val creditSettings: List<DisplaySetting> = listOf(),
    val unitChangedEvent: Event<Unit>? = null,
    val themeChangedEvent: Event<Unit>? = null,
    val openLinkEvent: Event<String>? = null
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