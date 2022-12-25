package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.ForecastProvider

class GetForecastProvider internal constructor(
    private val settingsRepository: SettingsRepository
){
    suspend operator fun invoke(): ForecastProvider = settingsRepository.getForecastProvider()
}