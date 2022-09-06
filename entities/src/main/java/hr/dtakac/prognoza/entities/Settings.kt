package hr.dtakac.prognoza.entities

import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

data class Settings(
    val temperatureUnit: TemperatureUnit,
    val precipitationUnit: LengthUnit,
    val windUnit: SpeedUnit,
    val pressureUnit: PressureUnit,
    val place: Place
)