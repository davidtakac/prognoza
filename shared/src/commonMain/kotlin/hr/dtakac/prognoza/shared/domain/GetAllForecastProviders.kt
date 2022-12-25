package hr.dtakac.prognoza.shared.domain

import hr.dtakac.prognoza.shared.entity.ForecastProvider

class GetAllForecastProviders {
    suspend operator fun invoke(): List<ForecastProvider> = ForecastProvider.values().toList()
}