package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

data class Forecast(
    private val timeZone: TimeZone,
    val days: List<Day>
) {
    val futureDays: List<Day>
        get() = days.filter {
            val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
            val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
            dayDate >= nowDate
        }

    val futureHours: List<Hour>
        get() = futureDays.flatMap(Day::hours)
}

data class Day(
    val startUnixSecond: Long,
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

    val futureHours: List<Hour>
        get() = hours.filter {
            it.startUnixSecond >= Clock.System.now().epochSeconds
        }

    private fun throwInvalidHours(): Nothing =
        throw IllegalStateException("Hours must not be empty.")
}

data class Hour(
    val startUnixSecond: Long,
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