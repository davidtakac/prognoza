package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KProperty1

class Forecast internal constructor(
  val timeZone: TimeZone,
  val hours: List<Hour>,
  val sunriseEpochSeconds: List<Long>,
  val sunsetEpochSeconds: List<Long>,
) {
  private val days: List<Day> = hours
    .groupBy { Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date }
    .map { (_, hoursInDay) -> Day(hoursInDay) }

  val now: Hour = hours.firstOrNull {
    val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone)
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
    hourDateTime.date == nowDateTime.date && hourDateTime.time.hour == nowDateTime.time.hour
  } ?: throw OutdatedForecastException()

  val futureDays: List<Day> = days.dropWhile {
    val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    dayDate <= nowDate
  }

  val today: Day = days.firstOrNull {
    val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    dayDate == nowDate
  } ?: throw OutdatedForecastException()

  val beforeNow: List<Hour> = hours.takeWhile {
    val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).normalizeToHour()
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone).normalizeToHour()
    hourDateTime < nowDateTime
  }

  val restOfToday: Day = Day(today.hours - beforeNow.toSet())

  val fromNow: List<Hour> = hours - beforeNow.toSet()

  val rainAndShowersToday: PrecipitationToday = PrecipitationToday(this, Hour::rainAndShowers, Day::rainAndShowers)

  val snowToday: PrecipitationToday = PrecipitationToday(this, Hour::snow, Day::snow)

  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Forecast = Forecast(
    timeZone = timeZone,
    hours = hours.map { it.toMeasurementSystem(measurementSystem) },
    sunriseEpochSeconds = sunriseEpochSeconds,
    sunsetEpochSeconds = sunsetEpochSeconds
  )
}

class PrecipitationToday internal constructor(
  forecast: Forecast,
  hourlyGetter: KProperty1<Hour, Length>,
  dailyGetter: KProperty1<Day, Length>
) {
  val hoursInLastPeriod: Int
  val amountInLastPeriod: Length
  val hoursInNextPeriod: Int
  val amountInNextPeriod: Length
  val startUnixSecondOfNextWetDay: Long?
  val amountInNextWetDay: Length

  init {
    val unit = hourlyGetter.get(forecast.now).unit
    val pastHours = forecast.beforeNow.take(24)
    val futureHours = forecast.fromNow.take(24)
    val firstWetDayBesidesToday = (forecast.futureDays - forecast.today).firstOrNull { dailyGetter.get(it).value > 0 }
    hoursInLastPeriod = pastHours.size.takeUnless { it == 0 } ?: 1
    amountInLastPeriod = pastHours.fold(Length(0.0, unit)) { acc, hour -> acc + hourlyGetter.get(hour) }
    hoursInNextPeriod = futureHours.size
    amountInNextPeriod = futureHours.fold(Length(0.0, unit)) { acc, hour -> acc + hourlyGetter.get(hour) }
    startUnixSecondOfNextWetDay = firstWetDayBesidesToday?.startUnixSecond
    amountInNextWetDay = firstWetDayBesidesToday?.let(dailyGetter) ?: Length(0.0, unit)
  }
}

class OutdatedForecastException : Exception()

private fun LocalDateTime.normalizeToHour() = LocalDateTime(
  year = year,
  month = month,
  dayOfMonth = dayOfMonth,
  hour = hour,
  minute = 0,
  second = 0,
  nanosecond = 0
)