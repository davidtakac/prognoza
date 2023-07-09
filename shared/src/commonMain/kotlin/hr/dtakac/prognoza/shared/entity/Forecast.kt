package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

class Forecast internal constructor(
    val timeZone: TimeZone,
    val days: List<Day>
) {
    val futureDays: List<Day>
        get() = days.filter {
            val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
            val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
            dayDate >= nowDate
        }

    val futureHours: List<Hour>
        get() = days.flatMap { it.hours }.filter {
            val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone)
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
    val hours: List<Hour>
) {
    val representativeWmoCode: RepresentativeWmoCode by lazy {
        if (hours.any { it.wmoCode > 48 }) {
            // The most severe weather condition is at least light drizzle. Because this involves
            // precipitation, it could affect people's plans for the day. In this case they would
            // likely want to know the most severe weather condition so they can prepare accordingly
            val mostExtremeHour = hours.maxBy { it.wmoCode }
            RepresentativeWmoCode(mostExtremeHour.wmoCode, mostExtremeHour.isDay)
        } else {
            // The most severe weather condition is depositing rime fog. Because this doesn't
            // involve precipitation, it rarely affects people's plans for the day. In this case
            // they would likely want to know what most of the day looks like instead
            val priorityHours = hours
                // Prioritize daytime if possible
                .filter { it.isDay }
                .takeUnless { it.isEmpty() } ?: hours
            val mostCommonWmoCode = priorityHours
                // Map WMO code to amount of times it appears in the list
                .groupingBy { it.wmoCode }.eachCount()
                // Find the most severe WMO code (sortedByDescending) that appears the most (maxBy)
                .entries.sortedByDescending { it.key }.maxBy { it.value }.key
            RepresentativeWmoCode(mostCommonWmoCode, priorityHours[0].isDay)
        }
    }
}

class RepresentativeWmoCode internal constructor(
    val wmoCode: Int,
    val isDay: Boolean
)

class Hour internal constructor(
    val startUnixSecond: Long,
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
    val isDay: Boolean,
    val feelsLike: Temperature
)