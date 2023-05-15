package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

data class Forecast(
    val localTimeZone: TimeZone,
    val hours: List<Hour>,
    val days: List<Day>
) {
    init {
        if (hours.isEmpty()) throw IllegalStateException("Hours must not be empty.")
        if (days.isEmpty()) throw IllegalStateException("Days must not be empty.")
    }

    val futureHours: List<Hour>
        get() = hours.filter { it.unixSecond >= Clock.System.now().epochSeconds }

    val futureDays: List<Day>
        get() = days.filter { Instant.fromEpochSeconds(it.unixSecond).toLocalDateTime(localTimeZone).date >= Clock.System.now().toLocalDateTime(localTimeZone).date }
}

data class Hour(
    val unixSecond: Long,
    val wmoCode: Int,
    val temperature: Temperature,
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val precipitation: Length,
    val probabilityOfPrecipitation: Percentage,
    val wind: Speed,
    val gust: Speed,
    val windDirection: Angle,
    val pressureAtSeaLevel: Pressure,
    val relativeHumidity: Percentage,
    val dewPoint: Temperature,
    val visibility: Length,
    val uvIndex: Double,
    val day: Boolean,
    val feelsLike: Temperature
)

data class Day(
    val unixSecond: Long,
    val wmoCode: Int,
    val sunriseUnixSecond: Long?,
    val sunsetUnixSecond: Long?,
    val minimumTemperature: Temperature,
    val maximumTemperature: Temperature,
    val minimumFeelsLike: Temperature,
    val maximumFeelsLike: Temperature,
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val precipitation: Length,
    val maximumProbabilityOfPrecipitation: Percentage,
    val maximumWind: Speed,
    val maximumGust: Speed,
    val dominantWindDirection: Angle,
    val maximumUvIndex: Double
)