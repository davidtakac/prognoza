package hr.dtakac.prognoza.shared.domain

class GetAllForecastProviders {
    suspend operator fun invoke(): List<ForecastProvider> = ForecastProvider.values().toList()
}