package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val temperature: Int?,
    val feelsLike: Int?,
    val precipitationAmount: Float?,
    val windSpeed: Float?,
    val windIconRotation: Float?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime
)