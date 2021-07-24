package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class TomorrowUiModel(
    val dateTime: ZonedDateTime,
    val lowTemperature: Int,
    val highTemperature: Int,
    val weatherIcon: WeatherIcon?,
    val hours: List<HourUiModel>
)