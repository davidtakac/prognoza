package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.PressureUnit

class GetAllPressureUnits internal constructor() {
    suspend operator fun invoke(): List<PressureUnit> = PressureUnit.values().toList()
}