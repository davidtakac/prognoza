package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class TodayUiModel(
    val dateTime: ZonedDateTime,
    val currentTemperature: Int?,
    val weatherIcon: WeatherIcon?,
    val nextHours: List<HourUiModel>
)