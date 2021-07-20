package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class TomorrowUiModel(
    val dateTime: ZonedDateTime,
    val lowTemperature: Short,
    val highTemperature: Short,
    val weatherIcon: WeatherIcon,
    val hours: List<HourUiModel>
)