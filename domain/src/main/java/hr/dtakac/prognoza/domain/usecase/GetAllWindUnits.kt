package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit

class GetAllWindUnits {
    suspend operator fun invoke(): List<SpeedUnit> = SpeedUnit.values().toList()
}