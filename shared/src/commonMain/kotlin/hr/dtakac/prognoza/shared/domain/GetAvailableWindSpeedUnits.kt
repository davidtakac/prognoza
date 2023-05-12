package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.SpeedUnit

class GetAvailableWindSpeedUnits internal constructor() {
    operator fun invoke(): List<SpeedUnit> = SpeedUnit.values().toList()
}