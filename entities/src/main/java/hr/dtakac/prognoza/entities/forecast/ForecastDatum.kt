package hr.dtakac.prognoza.entities.forecast

import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class ForecastDatum(
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val temperature: Temperature,
    val precipitation: Length,
    val wind: Wind,
    val airPressureAtSeaLevel: Pressure,
    val description: ForecastDescription,
    val humidity: Percentage
) {
    val feelsLike: Temperature = calculateFeelsLikeTemperature()
    val shortDescription: ShortForecastDescription = calculateShortForecastDescription()

    private fun calculateFeelsLikeTemperature(): Temperature {
        val tempFahrenheit = temperature.fahrenheit
        val feelsLikeFahrenheit = if (tempFahrenheit in -50.0..50.0) {
            calculateWindChill(tempFahrenheit, wind.speed.milesPerHour)
        } else {
            calculateHeatIndex(tempFahrenheit, humidity.percent)
        }
        return Temperature(feelsLikeFahrenheit, TemperatureUnit.F)
    }

    /**
     * Calculates wind chill. Assumes [airTemperature] in degrees Fahrenheit and [windSpeed] in miles per hour.
     *
     * Based on [The Wind Chill Equation](https://www.weather.gov/media/epz/wxcalc/windChill.pdf)
     *
     * @return Wind chill temperature in degrees Fahrenheit
     */
    private fun calculateWindChill(
        airTemperature: Double,
        windSpeed: Double
    ): Double {
        return 35.74 + (0.6215 * airTemperature) - (35.75 * windSpeed.pow(0.16)) + (0.4275 * airTemperature * windSpeed.pow(
            0.16
        ))
    }

    /**
     * Calculates heat index. Assumes [airTemperature] in degrees Fahrenheit and [humidity] in percent.
     *
     * Based on [The Heat Index Equation](https://www.wpc.ncep.noaa.gov/html/heatindex_equation.shtml)
     *
     * @return Heat index temperature in degrees Fahrenheit
     */
    private fun calculateHeatIndex(
        airTemperature: Double,
        humidity: Double
    ): Double {
        val simpleHeatIndex =
            0.5 * (airTemperature + 61 + (airTemperature - 68) * 1.2 + humidity * 0.094)
        return if (simpleHeatIndex < 80) {
            simpleHeatIndex
        } else {
            val rothfuszHeatIndex = -42.379 + 2.04901523 * airTemperature + 10.14333127 * humidity -
                    0.22475541 * airTemperature * humidity - 0.00683783 * airTemperature * airTemperature -
                    0.05481717 * humidity * humidity + 0.00122874 * airTemperature * airTemperature * humidity +
                    0.00085282 * airTemperature * humidity * humidity - 0.00000199 * airTemperature * airTemperature * humidity * humidity

            if (humidity < 13 && airTemperature in 80.0..112.0) {
                val adjustment = ((13 - humidity) / 4) * sqrt((17 - abs(airTemperature - 95)) / 17)
                rothfuszHeatIndex - adjustment
            } else if (humidity > 85 && airTemperature in 80.0..87.0) {
                val adjustment = ((humidity - 85) / 10) * ((87 - airTemperature) / 5)
                rothfuszHeatIndex + adjustment
            } else {
                rothfuszHeatIndex
            }
        }
    }

    private fun calculateShortForecastDescription(): ShortForecastDescription = when (description) {
        ForecastDescription.CLEAR_SKY_DAY,
        ForecastDescription.CLEAR_SKY_NIGHT,
        ForecastDescription.CLEAR_SKY_POLAR_TWILIGHT,
        ForecastDescription.FAIR_DAY,
        ForecastDescription.FAIR_NIGHT,
        ForecastDescription.FAIR_POLAR_TWILIGHT,
        ForecastDescription.FOG,
        ForecastDescription.UNKNOWN -> ShortForecastDescription.CLEAR

        ForecastDescription.CLOUDY,
        ForecastDescription.PARTLY_CLOUDY_DAY,
        ForecastDescription.PARTLY_CLOUDY_NIGHT,
        ForecastDescription.PARTLY_CLOUDY_POLAR_TWILIGHT -> ShortForecastDescription.CLOUDY

        ForecastDescription.HEAVY_RAIN_AND_THUNDER,
        ForecastDescription.HEAVY_RAIN,
        ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.HEAVY_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.HEAVY_RAIN_SHOWERS_DAY,
        ForecastDescription.HEAVY_RAIN_SHOWERS_NIGHT,
        ForecastDescription.HEAVY_RAIN_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_RAIN_AND_THUNDER,
        ForecastDescription.LIGHT_RAIN,
        ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.LIGHT_RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_RAIN_SHOWERS_DAY,
        ForecastDescription.LIGHT_RAIN_SHOWERS_NIGHT,
        ForecastDescription.LIGHT_RAIN_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.RAIN_AND_THUNDER,
        ForecastDescription.RAIN,
        ForecastDescription.RAIN_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.RAIN_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.RAIN_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.RAIN_SHOWERS_DAY,
        ForecastDescription.RAIN_SHOWERS_NIGHT,
        ForecastDescription.RAIN_SHOWERS_POLAR_TWILIGHT -> ShortForecastDescription.RAIN

        ForecastDescription.HEAVY_SLEET_AND_THUNDER,
        ForecastDescription.HEAVY_SLEET,
        ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.HEAVY_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.HEAVY_SLEET_SHOWERS_DAY,
        ForecastDescription.HEAVY_SLEET_SHOWERS_NIGHT,
        ForecastDescription.HEAVY_SLEET_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_SLEET_AND_THUNDER,
        ForecastDescription.LIGHT_SLEET,
        ForecastDescription.LIGHT_SLEET_SHOWERS_DAY,
        ForecastDescription.LIGHT_SLEET_SHOWERS_NIGHT,
        ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.LIGHT_SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_SLEET_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.SLEET_AND_THUNDER,
        ForecastDescription.SLEET,
        ForecastDescription.SLEET_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.SLEET_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.SLEET_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.SLEET_SHOWERS_DAY,
        ForecastDescription.SLEET_SHOWERS_NIGHT,
        ForecastDescription.SLEET_SHOWERS_POLAR_TWILIGHT -> ShortForecastDescription.SLEET

        ForecastDescription.HEAVY_SNOW_AND_THUNDER,
        ForecastDescription.HEAVY_SNOW,
        ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.HEAVY_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.HEAVY_SNOW_SHOWERS_DAY,
        ForecastDescription.HEAVY_SNOW_SHOWERS_NIGHT,
        ForecastDescription.HEAVY_SNOW_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_SNOW_AND_THUNDER,
        ForecastDescription.LIGHT_SNOW,
        ForecastDescription.LIGHT_SNOW_SHOWERS_DAY,
        ForecastDescription.LIGHT_SNOW_SHOWERS_NIGHT,
        ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.LIGHT_SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.LIGHT_SNOW_SHOWERS_POLAR_TWILIGHT,
        ForecastDescription.SNOW_AND_THUNDER,
        ForecastDescription.SNOW,
        ForecastDescription.SNOW_SHOWERS_AND_THUNDER_DAY,
        ForecastDescription.SNOW_SHOWERS_AND_THUNDER_NIGHT,
        ForecastDescription.SNOW_SHOWERS_AND_THUNDER_POLAR_TWILIGHT,
        ForecastDescription.SNOW_SHOWERS_DAY,
        ForecastDescription.SNOW_SHOWERS_NIGHT,
        ForecastDescription.SNOW_SHOWERS_POLAR_TWILIGHT -> ShortForecastDescription.SNOW
    }
}