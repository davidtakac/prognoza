package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.WeatherDescription
import java.time.ZonedDateTime

data class TemperatureUiModel(
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime?,
    val weatherDescription: WeatherDescription?,
    val instantTemperature: Double?,
    val feelsLike: Double?
)