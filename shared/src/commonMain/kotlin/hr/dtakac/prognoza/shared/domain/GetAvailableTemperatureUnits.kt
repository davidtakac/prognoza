package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.TemperatureUnit

class GetAvailableTemperatureUnits internal constructor() {
    operator fun invoke(): List<TemperatureUnit> = TemperatureUnit.values().toList()
}