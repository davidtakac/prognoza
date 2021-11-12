package hr.dtakac.prognoza.forecast.model

import hr.dtakac.prognoza.core.model.ui.RepresentativeWeatherDescription

data class PeriodOfDayUiModel(
    val lowestTemperature: Double?,
    val highestTemperature: Double?,
    val totalPrecipitation: Double?,
    val representativeWeatherDescription: RepresentativeWeatherDescription?
)