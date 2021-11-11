package hr.dtakac.prognoza.core.utils

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
    temperature: Double,
    windSpeed: Double?,
    relativeHumidity: Double?
): Double? {
    val airTemperatureFahrenheit = temperature.degreesCelsiusToDegreesFahrenheit()
    val feelsLikeFahrenheit = if (airTemperatureFahrenheit in -50.0..50.0) {
        if (windSpeed == null) {
            null
        } else {
            val windSpeedMph = windSpeed.metersPerSecondToMilesPerHour()
            calculateWindChill(airTemperatureFahrenheit, windSpeedMph)
        }
    } else {
        if (relativeHumidity == null) {
            null
        } else {
            calculateHeatIndex(airTemperatureFahrenheit, relativeHumidity)
        }
    }
    return feelsLikeFahrenheit?.degreesFahrenheitToDegreesCelsius()
}

/**
 * Calculates wind chill. Assumes [temperature] in degrees Fahrenheit and [windSpeed] in miles per hour.
 *
 * Based on [The Wind Chill Equation](https://www.weather.gov/media/epz/wxcalc/windChill.pdf)
 *
 * @return Wind chill temperature in degrees Fahrenheit
 */
private fun calculateWindChill(
    temperature: Double,
    windSpeed: Double
): Double {
    return 35.74 + (0.6215 * temperature) - (35.75 * windSpeed.pow(0.16)) +
            (0.4275 * temperature * windSpeed.pow(0.16))
}

/**
 * Calculates heat index. Assumes [temperature] in degrees Fahrenheit and [humidity] in percent.
 *
 * Based on [The Heat Index Equation](https://www.wpc.ncep.noaa.gov/html/heatindex_equation.shtml)
 *
 * @return Heat index temperature in degrees Fahrenheit
 */
private fun calculateHeatIndex(
    temperature: Double,
    humidity: Double
): Double {
    val simpleHeatIndex = 0.5 * (temperature + 61 + (temperature - 68) * 1.2 + humidity * 0.094)
    return if (simpleHeatIndex < 80) {
        simpleHeatIndex
    } else {
        val rothfuszHeatIndex = -42.379 + 2.04901523 * temperature + 10.14333127 * humidity -
                0.22475541 * temperature * humidity - 0.00683783 * temperature * temperature -
                0.05481717 * humidity * humidity + 0.00122874 * temperature * temperature * humidity +
                0.00085282 * temperature * humidity * humidity - 0.00000199 * temperature * temperature * humidity * humidity

        if (humidity < 13 && temperature in 80.0..112.0) {
            val adjustment = ((13 - humidity) / 4) * sqrt((17 - abs(temperature - 95)) / 17)
            rothfuszHeatIndex - adjustment
        } else if (humidity > 85 && temperature in 80.0..87.0) {
            val adjustment = ((humidity - 85) / 10) * ((87 - temperature) / 5)
            rothfuszHeatIndex + adjustment
        } else {
            rothfuszHeatIndex
        }
    }
}