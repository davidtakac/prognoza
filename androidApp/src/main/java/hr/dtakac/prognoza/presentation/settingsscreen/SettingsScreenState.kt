package hr.dtakac.prognoza.presentation.settingsscreen

import hr.dtakac.prognoza.presentation.Event
import hr.dtakac.prognoza.presentation.TextResource

data class SettingsScreenState(
    val isLoading: Boolean = false,
    val unitSettings: List<MultipleChoiceSettingUi> = listOf(),
    val appearanceSettings: List<MultipleChoiceSettingUi> = listOf(),
    val creditSettings: List<DisplaySettingUi> = listOf(),
    val unitChangedEvent: Event<Unit>? = null,
    val themeChangedEvent: Event<Unit>? = null,
    val moodModeChangedEvent: Event<Unit>? = null,
    val openLinkEvent: Event<String>? = null
)

data class MultipleChoiceSettingUi(
    val name: TextResource,
    val selectedIndex: Int,
    val values: List<TextResource>,
    val onIndexSelected: (Int) -> Unit
)

data class DisplaySettingUi(
    val name: TextResource,
    val value: TextResource,
    val onClick: () -> Unit
)