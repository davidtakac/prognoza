package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

class Forecast internal constructor(
    private val localTimeZone: TimeZone,
    val hours: List<Hour>,
    val days: List<Day>
) {
    init {
        if (hours.isEmpty()) throwInvalidHours()
        if (days.isEmpty()) throwInvalidDays()
    }

    val futureHours: List<Hour> get() = hours.filter {
        it.unixSecond >= Clock.System.now().epochSeconds
    }

    val futureDays: List<Day> get() = days.filter {
        val dayDate = Instant.fromEpochSeconds(it.unixSecond).toLocalDateTime(localTimeZone).date
        val nowDate = Clock.System.now().toLocalDateTime(localTimeZone).date
        dayDate >= nowDate
    }

    private fun throwInvalidHours(): Nothing =
        throw IllegalStateException("Hours must not be empty.")

    private fun throwInvalidDays(): Nothing =
        throw IllegalStateException("Days must not be empty.")
}

data class Hour(
    val unixSecond: Long,
    val wmoCode: Int,
    val temperature: Temperature,
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val probabilityOfPrecipitation: Double,
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
    val maximumProbabilityOfPrecipitation: Double,
    val maximumWind: Speed,
    val maximumGust: Speed,
    val dominantWindDirection: Angle,
    val maximumUvIndex: UvIndex
)