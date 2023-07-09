package hr.dtakac.prognoza.shared.usecase

import hr.dtakac.prognoza.shared.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.MeasurementSystem

class GetSelectedMeasurementSystem internal constructor(private val settingsRepository: SettingsRepository) {
  suspend operator fun invoke(): MeasurementSystem = settingsRepository.getMeasurementSystem()
}