package hr.dtakac.prognoza.forecast.uimodel

import hr.dtakac.prognoza.WeatherIcon
import java.time.LocalDateTime

data class HourUiModel(
    val temperature: Float,
    val precipitationAmount: Float?,
    val weatherIcon: WeatherIcon,
    val dateTime: LocalDateTime
)