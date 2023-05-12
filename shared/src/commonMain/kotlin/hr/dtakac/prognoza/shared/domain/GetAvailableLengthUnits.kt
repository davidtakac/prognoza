package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.LengthUnit

class GetAvailableLengthUnits internal constructor() {
    operator fun invoke(): List<LengthUnit> = LengthUnit.values().toList()
}