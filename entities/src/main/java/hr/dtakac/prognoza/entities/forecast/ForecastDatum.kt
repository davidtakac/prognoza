package hr.dtakac.prognoza.entities.forecast

import hr.dtakac.prognoza.entities.forecast.units.*
import java.time.ZonedDateTime
import kotlin.math.pow

data class ForecastDatum(
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Wind,
    val airPressure: Pressure,
    val description: Description,
    val mood: Mood = Mood.fromDescription(description),
    val humidity: Percentage,
    val feelsLike: Temperature = calculateWindChill(temperature, wind.speed)
)

// Feels like temperature is accurately represented by JAG/TI Wind Chill factor
// https://www.weather.gov/media/lsx/wcm/Winter2008/Wind_Chill.pdf
fun calculateWindChill(
    temperature: Temperature,
    windSpeed: Speed
): Temperature = Temperature(
    value = 35.74 +
            (0.6215 * temperature.fahrenheit) -
            (35.75 * windSpeed.milePerHour.pow(0.16)) +
            (0.4275 * temperature.fahrenheit * windSpeed.milePerHour.pow(0.16)),
    unit = TemperatureUnit.DEGREE_FAHRENHEIT
)