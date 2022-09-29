package hr.dtakac.prognoza.presentation.settings

import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import hr.dtakac.prognoza.presentation.TextResource

fun mapToTemperatureUnitSetting(
    selectedTemperatureUnit: TemperatureUnit,
    availableTemperatureUnits: List<TemperatureUnit>
): TemperatureUnitSetting = TemperatureUnitSetting(
    name = TextResource.fromStringId(R.string.temperature_unit),
    value = TextResource.fromStringId(selectedTemperatureUnit.toSettingsLabel()),
    values = availableTemperatureUnits.map(TemperatureUnit::toSettingsLabel).map(TextResource::fromStringId)
)