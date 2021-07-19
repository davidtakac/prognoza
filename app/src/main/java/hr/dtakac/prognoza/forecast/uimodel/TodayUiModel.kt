package hr.dtakac.prognoza.forecast.uimodel

import hr.dtakac.prognoza.WeatherIcon
import java.time.LocalDateTime

data class TodayUiModel(
    val dateTime: LocalDateTime,
    val currentTemperature: Short,
    val weatherIcon: WeatherIcon,
    val nextHours: List<HourUiModel>
)