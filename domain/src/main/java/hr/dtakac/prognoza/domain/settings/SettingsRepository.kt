package hr.dtakac.prognoza.domain.settings

import hr.dtakac.prognoza.entities.Place
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

interface SettingsRepository {
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
    suspend fun getTemperatureUnit(): TemperatureUnit

    suspend fun setPrecipitationUnit(unit: LengthUnit)
    suspend fun getPrecipitationUnit(): LengthUnit

    suspend fun setWindUnit(unit: SpeedUnit)
    suspend fun getWindUnit(): SpeedUnit

    suspend fun setPressureUnit(unit: PressureUnit)
    suspend fun getPressureUnit(): PressureUnit

    suspend fun setPlace(place: Place)
    suspend fun getPlace(): Place?
}