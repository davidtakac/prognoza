package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val temperature: Int?,
    val feelsLike: Int?,
    val precipitation: Float?,
    val windSpeed: Float?,
    val windIconRotation: Float?,
    val windFromCompassDirection: Int?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime,
    val relativeHumidity: Float?,
    val pressure: Float?
)