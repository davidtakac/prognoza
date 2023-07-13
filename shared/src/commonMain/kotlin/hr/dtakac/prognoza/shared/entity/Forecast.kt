package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*
import kotlin.reflect.KProperty1

class Forecast internal constructor(
  val timeZone: TimeZone,
  val days: List<Day>
) {
  val today: Day = days.firstOrNull {
    val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    dayDate == nowDate
  } ?: throw OutdatedForecastException()

  val now: Hour = today.hours.firstOrNull {
    val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone)
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
    hourDateTime.date == nowDateTime.date && hourDateTime.time.hour == nowDateTime.time.hour
  } ?: throw OutdatedForecastException()

  val untilToday: List<Day> = days.filter {
    val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    dayDate <= nowDate
  }

  val fromToday: List<Day> = days.filter {
    val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    dayDate >= nowDate
  }

  val latestRainAndShowers: PrecipitationPeriod =
    getPrecipitationPeriod(Hour::rainAndShowers, untilToday.flatMap { it.hours }.take(24))
  val latestSnow: PrecipitationPeriod =
    getPrecipitationPeriod(Hour::snow, untilToday.flatMap { it.hours }.take(24))
  val soonestRainAndShowers: PrecipitationPeriod =
    getPrecipitationPeriod(Hour::rainAndShowers, fromToday.flatMap { it.hours }.take(24))
  val soonestSnow: PrecipitationPeriod =
    getPrecipitationPeriod(Hour::snow, fromToday.flatMap { it.hours }.take(24))

  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Forecast = Forecast(
    timeZone = timeZone,
    days = days.map { it.toMeasurementSystem(measurementSystem) }
  )

  private fun getPrecipitationPeriod(
    precipitationGetter: KProperty1<Hour, Length>,
    hours: List<Hour>
  ) = PrecipitationPeriod(
    value = hours.fold(
      Length(0.0, precipitationGetter.get(hours[0]).unit)
    ) { acc, hour -> acc + precipitationGetter.get(hour) },
    hours = hours.size
  )
}

class PrecipitationPeriod internal constructor(
  val value: Length,
  val hours: Int
)

class OutdatedForecastException : Exception()