package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.di.ComputationDispatcher
import hr.dtakac.prognoza.presentation.settingsscreen.toSettingsLabel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsScreenUiMapper @Inject constructor(@ComputationDispatcher private val computationDispatcher: CoroutineDispatcher) {
    suspend fun mapToUiModeSetting(
        selected: UiMode,
        options: List<UiMode>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromResId(R.string.settings_title_theme),
        selectedIndex = options.indexOf(selected),
        values = withContext(computationDispatcher) {
            options.map(UiMode::toSettingsLabel).map(TextResource::fromResId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToMoodModeSetting(
        selected: MoodMode,
        options: List<MoodMode>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromResId(R.string.settings_title_mood),
        selectedIndex = options.indexOf(selected),
        values = withContext(computationDispatcher) {
            options.map(MoodMode::toSettingsLabel).map(TextResource::fromResId)
        },
        onIndexSelected = onIndexSelected
    )
}