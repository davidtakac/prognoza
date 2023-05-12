package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.*

class SettingsRepository {
    suspend fun setTemperatureUnit(unit: TemperatureUnit) {TODO()}
    suspend fun getTemperatureUnit(): TemperatureUnit {TODO()}
    suspend fun setPrecipitationUnit(unit: LengthUnit) {TODO()}
    suspend fun getPrecipitationUnit(): LengthUnit {TODO()}
    suspend fun setWindUnit(unit: SpeedUnit) {TODO()}
    suspend fun getWindUnit(): SpeedUnit {TODO()}
    suspend fun setPressureUnit(unit: PressureUnit) {TODO()}
    suspend fun getPressureUnit(): PressureUnit {TODO()}
    suspend fun setSelectedPlaceId(id: String) {TODO()}
    // todo: pull from local storage
    suspend fun getSelectedPlaceId(): String? = "1"
}