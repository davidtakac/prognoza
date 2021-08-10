package hr.dtakac.prognoza.forecast.uimodel

import java.time.ZonedDateTime

data class HourUiModel(
    val temperature: Float?,
    val feelsLike: Float?,
    val precipitation: Float?,
    val windSpeed: Float?,
    val windIconRotation: Float?,
    val windFromCompassDirection: Int?,
    val weatherIcon: WeatherIcon?,
    val time: ZonedDateTime,
    val relativeHumidity: Float?,
    val pressure: Float?
)