package hr.dtakac.prognoza.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val id: String,
    val temperature: Float?,
    val feelsLike: Float?,
    val precipitation: Float?,
    val windSpeed: Float?,
    val windIconRotation: Float?,
    val windFromCompassDirection: Int?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime,
    val relativeHumidity: Float?,
    val pressure: Float?,
    var isExpanded: Boolean = false
)