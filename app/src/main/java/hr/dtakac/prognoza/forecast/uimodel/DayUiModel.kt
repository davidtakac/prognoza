package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class DayUiModel(
    val time: ZonedDateTime,
    val weatherIcon: WeatherIcon?,
    val lowTemperature: Int,
    val highTemperature: Int,
    val precipitationAmount: Float?
)