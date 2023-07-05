package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.ui.common.Event
import hr.dtakac.prognoza.ui.common.TextResource

data class SettingsScreenState(
    val isLoading: Boolean = false,
    val unitSettings: List<MultipleChoiceSettingUi> = listOf(),
    val appearanceSettings: List<MultipleChoiceSettingUi> = listOf(),
    val updateForecastEvent: Event<Unit>? = null,
    val updateThemeEvent: Event<Unit>? = null
)

data class MultipleChoiceSettingUi(
    val name: TextResource,
    val selectedIndex: Int,
    val values: List<TextResource>,
    val onIndexSelected: (Int) -> Unit
)