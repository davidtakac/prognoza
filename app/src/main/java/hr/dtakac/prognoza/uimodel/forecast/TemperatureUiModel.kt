package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.WeatherDescription

data class TemperatureUiModel(
    val weatherDescription: WeatherDescription?,
    val airTemperature: Double?,
    val feelsLike: Double?
)