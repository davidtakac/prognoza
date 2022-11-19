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
        selectedTemperatureUnit: TemperatureUnit,
        availableTemperatureUnits: List<TemperatureUnit>,
        onValuePick: (Int) -> Unit
    ): MultipleChoiceSetting = MultipleChoiceSetting(
        name = TextResource.fromStringId(R.string.temperature_unit),
        value = TextResource.fromStringId(selectedTemperatureUnit.toSettingsLabel()),
        values = withContext(computationDispatcher) {
            availableTemperatureUnits.map(TemperatureUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onValuePick = onValuePick
    )

    suspend fun mapToWindUnitSetting(
        selectedWindUnit: SpeedUnit,
        availableWindUnits: List<SpeedUnit>,
        onValuePick: (Int) -> Unit
    ): MultipleChoiceSetting = MultipleChoiceSetting(
        name = TextResource.fromStringId(R.string.wind_unit),
        value = TextResource.fromStringId(selectedWindUnit.toSettingsLabel()),
        values = withContext(computationDispatcher) {
            availableWindUnits.map(SpeedUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onValuePick = onValuePick
    )

    suspend fun mapToPrecipitationUnitSetting(
        selectedPrecipitationUnit: LengthUnit,
        availablePrecipitationUnits: List<LengthUnit>,
        onValuePick: (Int) -> Unit
    ): MultipleChoiceSetting = MultipleChoiceSetting(
        name = TextResource.fromStringId(R.string.precipitation_unit),
        value = TextResource.fromStringId(selectedPrecipitationUnit.toSettingsLabel()),
        values = withContext(computationDispatcher) {
            availablePrecipitationUnits.map(LengthUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onValuePick = onValuePick
    )

    suspend fun mapToPressureUnitSetting(
        selectedPressureUnit: PressureUnit,
        availablePressureUnits: List<PressureUnit>,
        onValuePick: (Int) -> Unit
    ): MultipleChoiceSetting = MultipleChoiceSetting(
        name = TextResource.fromStringId(R.string.pressure_unit),
        value = TextResource.fromStringId(selectedPressureUnit.toSettingsLabel()),
        values = withContext(computationDispatcher) {
            availablePressureUnits.map(PressureUnit::toSettingsLabel).map(TextResource::fromStringId)
        },
        onValuePick = onValuePick
    )

    suspend fun mapToThemeSetting(
        selectedThemeSetting: ThemeSetting,
        availableThemeSettings: List<ThemeSetting>,
        onValuePick: (Int) -> Unit
    ): MultipleChoiceSetting = MultipleChoiceSetting(
        name = TextResource.fromStringId(R.string.theme),
        value = TextResource.fromStringId(selectedThemeSetting.toSettingsLabel()),
        values = withContext(computationDispatcher) {
            availableThemeSettings.map(ThemeSetting::toSettingsLabel).map(TextResource::fromStringId)
        },
        onValuePick = onValuePick
    )

    fun getForecastCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySetting = DisplaySetting(
        name = TextResource.fromStringId(R.string.weather_data),
        value = TextResource.fromStringId(R.string.met_norway_credit),
        onClick = onClick
    )

    fun getGeolocationCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySetting = DisplaySetting(
        name = TextResource.fromStringId(R.string.geolocation_data),
        value = TextResource.fromStringId(R.string.osm_nominatim_credit),
        onClick = onClick
    )

    fun getDesignCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySetting = DisplaySetting(
        name = TextResource.fromStringId(R.string.design_credit),
        value = TextResource.fromStringId(R.string.neal_hampton_credit),
        onClick = onClick
    )

    fun getIconCreditDisplaySetting(
        onClick: () -> Unit
    ): DisplaySetting = DisplaySetting(
        name = TextResource.fromStringId(R.string.launcher_icon_credit),
        value = TextResource.fromStringId(R.string.natasa_takac_credit),
        onClick = onClick
    )
}