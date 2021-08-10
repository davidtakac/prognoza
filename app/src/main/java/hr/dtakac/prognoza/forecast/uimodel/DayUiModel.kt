package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class DayUiModel(
    val time: ZonedDateTime,
    val weatherIcon: WeatherIcon?,
    val lowTemperature: Float?,
    val highTemperature: Float?,
    val maxWindSpeed: Float?,
    val totalPrecipitationAmount: Float?
)