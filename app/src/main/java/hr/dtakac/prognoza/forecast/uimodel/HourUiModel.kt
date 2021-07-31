package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val temperature: Int?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
    val windFromDirection: Float?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime
)