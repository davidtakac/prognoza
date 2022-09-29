package hr.dtakac.prognoza.domain.usecase

import hr.dtakac.prognoza.entities.forecast.units.LengthUnit

class GetAllPrecipitationUnits {
    suspend operator fun invoke(): List<LengthUnit> = LengthUnit.values().toList()
}