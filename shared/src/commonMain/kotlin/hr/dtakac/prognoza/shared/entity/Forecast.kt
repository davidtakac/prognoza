package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

class Forecast internal constructor(
    val timeZone: TimeZone,
    val hours: List<Hour>,
    val days: List<Day>
) {
    val futureDays: List<Day>
        get() = days.filter {
            val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
            val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
            dayDate >= nowDate
        }

    val futureHours: List<Hour>
        get() = hours.filter {
            val hourDateTime = Instant.fromEpochSeconds(it.unixSecond).toLocalDateTime(timeZone)
            val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
            val nowDateTimeNormalized = LocalDateTime(
                year = nowDateTime.year,
                month = nowDateTime.month,
                dayOfMonth = nowDateTime.dayOfMonth,
                hour = nowDateTime.hour,
                minute = 0,
                second = 0,
                nanosecond = 0
            )
            hourDateTime >= nowDateTimeNormalized
        }
}

class Day internal constructor(
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
    val maximumPop: Int,
    val maximumWind: Speed,
    val maximumGust: Speed,
    val dominantWindDirection: Angle,
    val maximumUvIndex: UvIndex,
)

class Hour internal constructor(
    val unixSecond: Long,
    val wmoCode: Int,
    val temperature: Temperature,
    val rain: Length,
    val showers: Length,
    val snow: Length,
    val totalPrecipitation: Length,
    val pop: Int,
    val wind: Speed,
    val gust: Speed,
    val windDirection: Angle,
    val pressureAtSeaLevel: Pressure,
    val relativeHumidity: Int,
    val dewPoint: Temperature,
    val visibility: Length,
    val uvIndex: UvIndex,
    val day: Boolean,
    val feelsLike: Temperature
)