package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit

class GetAllTemperatureUnits {
    suspend operator fun invoke(): List<TemperatureUnit> = TemperatureUnit.values().toList()
}