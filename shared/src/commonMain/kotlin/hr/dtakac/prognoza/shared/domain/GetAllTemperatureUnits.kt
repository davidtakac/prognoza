package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.TemperatureUnit

class GetAllTemperatureUnits internal constructor() {
    suspend operator fun invoke(): List<TemperatureUnit> = TemperatureUnit.values().toList()
}