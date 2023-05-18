package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.entity.MeasurementSystem

class GetAllMeasurementSystems internal constructor() {
    operator fun invoke(): List<MeasurementSystem> = MeasurementSystem.values().toList()
}