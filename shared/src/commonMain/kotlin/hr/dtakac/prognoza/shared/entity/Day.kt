package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Day internal constructor(
  val timeZone: TimeZone,
  val startUnixSecond: Long,
  val sunriseUnixSecond: Long?,
  val sunsetUnixSecond: Long?,
  val hours: List<Hour>
) {
  val minimumTemperature: Temperature = hours.minOf { it.temperature }
  val maximumTemperature: Temperature = hours.maxOf { it.temperature }
  val minimumFeelsLike: Temperature = hours.minOf { it.feelsLike }
  val maximumFeelsLike: Temperature = hours.maxOf { it.feelsLike }
  val rain: Length = hours.fold(Length(0.0, hours[0].rain.unit)) { acc, curr -> acc + curr.rain }
  val showers: Length = hours.fold(Length(0.0, hours[0].showers.unit)) { acc, curr -> acc + curr.showers }
  val rainAndShowers: Length = rain + showers
  val snow: Length = hours.fold(Length(0.0, hours[0].snow.unit)) { acc, curr -> acc + curr.snow }
  val maximumPop: Pop = hours.maxOf { it.pop }
  val maximumWind: Speed = hours.maxOf { it.wind }
  val maximumGust: Speed = hours.maxOf { it.gust }
  val maximumUvIndex: UvIndex = hours.maxOf { it.uvIndex }
  val representativeWmoCode: RepresentativeWmoCode = getRepresentativeWmoCode(hours)

  val untilNow: List<Hour> = hours.filter {
    val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).normalizeToHour()
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone).normalizeToHour()
    hourDateTime <= nowDateTime
  }

  val fromNow: List<Hour> = hours.filter {
    val hourDateTime =
      Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).normalizeToHour()
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone).normalizeToHour()
    hourDateTime >= nowDateTime
  }

  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Day =
    Day(
      timeZone = timeZone,
      startUnixSecond = startUnixSecond,
      sunriseUnixSecond = sunriseUnixSecond,
      sunsetUnixSecond = sunsetUnixSecond,
      hours = hours.map { it.toMeasurementSystem(measurementSystem) }
    )

  private fun getRepresentativeWmoCode(hours: List<Hour>): RepresentativeWmoCode =
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

  private fun LocalDateTime.normalizeToHour() = LocalDateTime(
    year = year,
    month = month,
    dayOfMonth = dayOfMonth,
    hour = hour,
    minute = 0,
    second = 0,
    nanosecond = 0
  )
}

class RepresentativeWmoCode internal constructor(
  val value: Int,
  val isDay: Boolean
)