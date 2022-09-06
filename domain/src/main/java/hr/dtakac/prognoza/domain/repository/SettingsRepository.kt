package hr.dtakac.prognoza.domain.repository

import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.Settings
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

interface SettingsRepository {
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
    suspend fun setPrecipitationUnit(unit: LengthUnit)
    suspend fun setWindUnit(unit: SpeedUnit)
    suspend fun setAirPressureUnit(unit: PressureUnit)
    suspend fun setPlace(place: Place)
    suspend fun get(): Settings
}