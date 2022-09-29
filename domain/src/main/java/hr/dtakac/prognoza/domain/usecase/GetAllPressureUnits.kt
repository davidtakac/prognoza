package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.entities.forecast.units.PressureUnit

class GetAllPressureUnits {
    suspend operator fun invoke(): List<PressureUnit> = PressureUnit.values().toList()
}