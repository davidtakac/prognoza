package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.domain.data.SettingsRepository
import hr.dtakac.prognoza.shared.entity.ForecastProvider

class SetForecastProvider internal constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(forecastProvider: ForecastProvider) =
        settingsRepository.setForecastProvider(forecastProvider)
}