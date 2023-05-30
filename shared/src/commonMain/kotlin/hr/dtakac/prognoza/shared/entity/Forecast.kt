package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

fun List<Day>.futureDays(): List<Day> =
    filter { Clock.System.now().epochSeconds - it.unixSecond >= -(24 * 60 * 60) }

data class Day(
    val unixSecond: Long,
    val mostExtremeWmoCode: Int,
    val sunriseUnixSecond: Long?,
    val sunsetUnixSecond: Long?,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val minimumFeelsLike: Temperature,
    val maximumFeelsLike: Temperature,
    val totalRain: Length,
    val totalShowers: Length,
    val totalSnow: Length,
    val maximumPop: Double,
    val maximumWind: Speed,
    val maximumGust: Speed,
    val dominantWindDirection: Angle,
    val maximumUvIndex: UvIndex,
    val hours: List<Hour>
) {
    init {
        if (hours.isEmpty()) throwInvalidHours()
    }

    val futureHours: List<Hour> get() = hours.filter {
        it.unixSecond >= Clock.System.now().epochSeconds
    }

    private fun throwInvalidHours(): Nothing =
        throw IllegalStateException("Hours must not be empty.")
}

data class Hour(
    val unixSecond: Long,
    val wmoCode: Int,
    val temperature: Temperature,
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val pop: Double,
    val wind: Speed,
    val gust: Speed,
    val windDirection: Angle,
    val pressureAtSeaLevel: Pressure,
    val relativeHumidity: Double,
    val dewPoint: Temperature,
    val visibility: Length,
    val uvIndex: UvIndex,
    val day: Boolean,
    val feelsLike: Temperature
)