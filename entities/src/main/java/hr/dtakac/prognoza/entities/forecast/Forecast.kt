package hr.dtakac.prognoza.entities.forecast

import hr.dtakac.prognoza.entities.forecast.units.*
import java.time.ZonedDateTime

data class Forecast(
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Speed,
    val windFromDirection: Angle,
    val airPressureAtSeaLevel: Pressure,
    val description: ForecastDescription,
    val humidity: Percentage
) {
    val feelsLike: Temperature = calculateFeelsLikeTemperature(
        airTemperature = temperature.fahrenheit,
        windSpeed = wind.milesPerHour,
        relativeHumidity = humidity.percent
    )?.let { Temperature(it, TemperatureUnit.FAHRENHEIT) } ?: temperature
}