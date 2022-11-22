package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.theme.ThemeSetting
import hr.dtakac.prognoza.shared.entity.LengthUnit
import hr.dtakac.prognoza.shared.entity.PressureUnit
import hr.dtakac.prognoza.shared.entity.SpeedUnit
import hr.dtakac.prognoza.shared.entity.TemperatureUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SettingsUiMapper @Inject constructor(
    @Named("computation")
    private val computationDispatcher: CoroutineDispatcher
) {
    suspend fun mapToTemperatureUnitSetting(
        selected: TemperatureUnit,
        units: List<TemperatureUnit>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.temperature_unit),
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
        name = TextResource.fromStringId(R.string.wind_unit),
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
        name = TextResource.fromStringId(R.string.precipitation_unit),
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
        name = TextResource.fromStringId(R.string.pressure_unit),
        selectedIndex = units.indexOf(selected),
        values = withContext(computationDispatcher) {
            units.map(PressureUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    suspend fun mapToThemeSetting(
        selected: ThemeSetting,
        options: List<ThemeSetting>,
        onIndexSelected: (Int) -> Unit
    ): MultipleChoiceSettingUi = MultipleChoiceSettingUi(
        name = TextResource.fromStringId(R.string.theme),
        selectedIndex = options.indexOf(selected),
        values = withContext(computationDispatcher) {
            options.map(ThemeSetting::toSettingsLabel).map(TextResource::fromStringId)
        },
        onIndexSelected = onIndexSelected
    )

    fun getForecastCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySettingUi = DisplaySettingUi(
        name = TextResource.fromStringId(R.string.weather_data),
        value = TextResource.fromStringId(R.string.met_norway_credit),
        onClick = onClick
    )

    fun getGeolocationCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySettingUi = DisplaySettingUi(
        name = TextResource.fromStringId(R.string.geolocation_data),
        value = TextResource.fromStringId(R.string.osm_nominatim_credit),
        onClick = onClick
    )

    fun getDesignCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySettingUi = DisplaySettingUi(
        name = TextResource.fromStringId(R.string.design_credit),
        value = TextResource.fromStringId(R.string.neal_hampton_credit),
        onClick = onClick
    )

    fun getIconCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySettingUi = DisplaySettingUi(
        name = TextResource.fromStringId(R.string.launcher_icon_credit),
        value = TextResource.fromStringId(R.string.natasa_takac_credit),
        onClick = onClick
    )
}