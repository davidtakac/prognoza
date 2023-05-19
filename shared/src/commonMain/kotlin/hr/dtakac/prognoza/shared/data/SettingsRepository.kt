package hr.dtakac.prognoza.shared.data

import hr.dtakac.prognoza.shared.entity.*

class SettingsRepository {
    suspend fun setMeasurementSystem(system: MeasurementSystem) {TODO()}
    suspend fun getMeasurementSystem(): MeasurementSystem = MeasurementSystem.Imperial
    suspend fun setCoordinates(coordinates: Coordinates) {TODO()}
    suspend fun getCoordinates(): Coordinates? = Coordinates(45.55111, 18.69389)
}