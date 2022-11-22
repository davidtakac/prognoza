package hr.dtakac.prognoza.shared.entity

import kotlin.math.pow

data class ForecastDatum(
    val startEpochMillis: Long,
    val endEpochMillis: Long,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Wind,
    val airPressure: Pressure,
    val description: Description,
    val mood: Mood = Mood.fromDescription(description),
    val humidity: Percentage,
    val feelsLike: Temperature = calculateWindChill(temperature, wind.speed)
)

/**
 * Calculates wind chill according to the
 * [weather.gov formula](https://www.weather.gov/media/lsx/wcm/Winter2008/Wind_Chill.pdf). It only
 * works for temperatures less than or equal to 50F and wind speeds greater than 3mph. See the
 * [weather.gov wind chill calculator](https://www.weather.gov/epz/wxcalc_windchill) for more info.
 *
 * @param temperature Air temperature
 * @param windSpeed Wind speed
 * @return Calculated wind chill temperature when [temperature] <= 50F and [windSpeed] > 3mph.
 * Else, [temperature].
 */
fun calculateWindChill(
    temperature: Temperature,
    windSpeed: Speed
): Temperature =
    if (temperature.fahrenheit <= 50.0 && windSpeed.milePerHour > 3.0) {
        Temperature(
            value = 35.74 +
                    (0.6215 * temperature.fahrenheit) -
                    (35.75 * windSpeed.milePerHour.pow(0.16)) +
                    (0.4275 * temperature.fahrenheit * windSpeed.milePerHour.pow(0.16)),
            unit = TemperatureUnit.DEGREE_FAHRENHEIT
        )
    } else temperature