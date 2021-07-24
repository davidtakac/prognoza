package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val temperature: Int?,
    val precipitationAmount: Float?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime
)