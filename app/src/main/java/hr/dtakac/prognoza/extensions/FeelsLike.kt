package hr.dtakac.prognoza.extensions

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Calculates the "Feels like" temperature. Assumes [temperature] in degrees Celsius,
 * [windSpeed] in m/s and [relativeHumidity] in percent.
 *
 * @return The "Feels like" temperature in degrees Celsius or null if it can't be calculated with
 * the provided parameters.
 */
fun calculateFeelsLikeTemperature(
    temperature: Float,
    windSpeed: Float?,
    relativeHumidity: Float?
): Float? {
    val airTemperatureFahrenheit = (temperature * 1.8f) + 32f
    val feelsLikeFahrenheit = if (airTemperatureFahrenheit in -50f..50f) {
        if (windSpeed == null) {
            null
        } else {
            val windSpeedMph = windSpeed * 2.236936f
            calculateWindChill(airTemperatureFahrenheit, windSpeedMph)
        }
    } else {
        if (relativeHumidity == null) {
            null
        } else {
            calculateHeatIndex(airTemperatureFahrenheit, relativeHumidity)
        }
    }
    return if (feelsLikeFahrenheit == null) null else (feelsLikeFahrenheit - 32f) / 1.8f
}

/**
 * Calculates wind chill. Assumes [temperature] in degrees Fahrenheit and [windSpeed] in miles per hour.
 *
 * Based on [The Wind Chill Equation](https://www.weather.gov/media/epz/wxcalc/windChill.pdf)
 *
 * @return Wind chill temperature in degrees Fahrenheit
 */
private fun calculateWindChill(
    temperature: Float,
    windSpeed: Float
): Float {
    return 35.74f + (0.6215f * temperature) - (35.75f * windSpeed.pow(0.16f)) +
            (0.4275f * temperature * windSpeed.pow(0.16f))
}

/**
 * Calculates heat index. Assumes [temperature] in degrees Fahrenheit and [humidity] in percent.
 *
 * Based on [The Heat Index Equation](https://www.wpc.ncep.noaa.gov/html/heatindex_equation.shtml)
 *
 * @return Heat index temperature in degrees Fahrenheit
 */
private fun calculateHeatIndex(
    temperature: Float,
    humidity: Float
): Float {
    val simpleHeatIndex = 0.5f * (temperature + 61 + (temperature - 68) * 1.2f + humidity * 0.094f)
    return if (simpleHeatIndex < 80f) {
        simpleHeatIndex
    } else {
        val rothfuszHeatIndex = -42.379f + 2.04901523f * temperature + 10.14333127f * humidity -
                0.22475541f * temperature * humidity - 0.00683783f * temperature * temperature -
                0.05481717f * humidity * humidity + 0.00122874f * temperature * temperature * humidity +
                0.00085282f * temperature * humidity * humidity - 0.00000199f * temperature * temperature * humidity * humidity

        if (humidity < 13f && temperature in 80f..112f) {
            val adjustment = ((13f - humidity) / 4) * sqrt((17f - abs(temperature - 95f)) / 17f)
            rothfuszHeatIndex - adjustment
        } else if (humidity > 85f && temperature in 80f..87f) {
            val adjustment = ((humidity - 85f) / 10f) * ((87f - temperature) / 5f)
            rothfuszHeatIndex + adjustment
        } else {
            rothfuszHeatIndex
        }
    }
}