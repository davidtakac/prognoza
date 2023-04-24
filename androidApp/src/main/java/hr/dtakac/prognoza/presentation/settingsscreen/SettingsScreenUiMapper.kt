package hr.dtakac.prognoza.presentation.settingsscreen

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.androidsettings.MoodMode
import hr.dtakac.prognoza.androidsettings.UiMode
import hr.dtakac.prognoza.di.ComputationDispatcher
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsScreenUiMapper @Inject constructor(@ComputationDispatcher private val computationDispatcher: CoroutineDispatcher) {
    suspend fun mapToTemperatureUnitSetting(
        selected: TemperatureUnit,
        units: List<TemperatureUnit>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_temperature),
        selectedIndex = units.indexOf(selected),
        values = withContext(computationDispatcher) {
            units.map(TemperatureUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToWindUnitSetting(
        selected: SpeedUnit,
        units: List<SpeedUnit>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_speed),
        selectedIndex = units.indexOf(selected),
        values = withContext(computationDispatcher) {
            units.map(SpeedUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToPrecipitationUnitSetting(
        selected: LengthUnit,
        units: List<LengthUnit>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_length),
        selectedIndex = units.indexOf(selected),
        values = withContext(computationDispatcher) {
            units.map(LengthUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToPressureUnitSetting(
        selected: PressureUnit,
        units: List<PressureUnit>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_pressure),
        selectedIndex = units.indexOf(selected),
        values = withContext(computationDispatcher) {
            units.map(PressureUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToUiModeSetting(
        selected: UiMode,
        options: List<UiMode>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_theme),
        selectedIndex = options.indexOf(selected),
        values = withContext(computationDispatcher) {
            options.map(UiMode::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToMoodModeSetting(
        selected: MoodMode,
        options: List<MoodMode>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.settings_title_mood),
        selectedIndex = options.indexOf(selected),
        values = withContext(computationDispatcher) {
            options.map(MoodMode::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )
}