package hr.dtakac.prognoza.shared.domain.data

import hr.dtakac.prognoza.shared.entity.*

internal interface SettingsRepository {
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