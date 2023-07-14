package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone
import kotlin.math.abs

class Overview internal constructor(forecast: Forecast) {
  val timeZone: TimeZone = forecast.timeZone
  val now: OverviewNow = OverviewNow(forecast.now, forecast.today)
  val hours: List<OverviewHour> = buildHours(
    hours = forecast.fromNow.take(24),
    sunrises = forecast.sunriseEpochSeconds,
    sunsets = forecast.sunsetEpochSeconds
  )
  val days: OverviewDays = OverviewDays(buildList { add(forecast.restOfToday); addAll(forecast.futureDays) })

  val rainfall: PrecipitationToday = forecast.rainAndShowersToday
  val snowfall: PrecipitationToday? = forecast.snowToday.takeIf {
    it.amountInLastPeriod.value > 0
        || it.amountInNextPeriod.value > 0
        || it.amountInNextWetDay.value > 0
  }
  val uvIndex: OverviewUvIndex = OverviewUvIndex(forecast.now, forecast.today)
  val feelsLike: OverviewFeelsLike = OverviewFeelsLike(forecast.now)
  val wind: OverviewWind = OverviewWind(forecast.now)
  val visibility: Length = forecast.now.visibility
  val nextSunriseUnixSecond: Long? = forecast.sunriseEpochSeconds.firstOrNull()
  val nextSunsetUnixSecond: Long? = forecast.sunsetEpochSeconds.firstOrNull()
  val humidity: OverviewHumidity = OverviewHumidity(forecast.now)

  private fun buildHours(
    hours: List<Hour>,
    sunrises: List<Long>,
    sunsets: List<Long>
  ) = buildList<OverviewHour> {
    addAll(hours.map { OverviewHour.Weather(it) })

    val sunriseHours = sunrises
      .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
      .map { OverviewHour.Sunrise(it) }
    addAll(sunriseHours)

    val sunsetHours = sunsets
      .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
      .map { OverviewHour.Sunset(it) }
    addAll(sunsetHours)

    // Ensures sunrises and sunsets are placed in between Weather hours
    sortBy { it.unixSecond }
  }
}

class OverviewNow internal constructor(now: Hour, today: Day) {
  val temperature: Temperature = now.temperature
  val minimumTemperature: Temperature = today.minimumTemperature
  val maximumTemperature: Temperature = today.maximumTemperature
  val feelsLike: Temperature = now.feelsLike
  val wmoCode: Int = now.wmoCode
  val isDay: Boolean = now.isDay
}

sealed interface OverviewHour {
  val unixSecond: Long

  class Weather internal constructor(hour: Hour) : OverviewHour {
    override val unixSecond: Long = hour.startUnixSecond
    val temperature: Temperature = hour.temperature
    val pop: Int = hour.pop.humanValue
    val wmoCode: Int = hour.wmoCode
    val isDay: Boolean = hour.isDay
  }

  class Sunrise internal constructor(override val unixSecond: Long) : OverviewHour

  class Sunset internal constructor(override val unixSecond: Long) : OverviewHour
}

class OverviewDays internal constructor(days: List<Day>) {
  val days: List<OverviewDay> = days.map { OverviewDay(it) }
  val minimumTemperature: Temperature = days.minOf { it.minimumTemperature }
  val maximumTemperature: Temperature = days.maxOf { it.maximumTemperature }
}

class OverviewDay internal constructor(day: Day) {
  val unixSecond: Long = day.startUnixSecond
  val representativeWmoCode: RepresentativeWmoCode = day.representativeWmoCode
  val minimumTemperature: Temperature = day.minimumTemperature
  val maximumTemperature: Temperature = day.maximumTemperature
  val maximumPop: Int = day.maximumPop.humanValue
}

class OverviewUvIndex internal constructor(now: Hour, today: Day) {
  val uvIndex: UvIndex = now.uvIndex
  val sunProtection: SunProtection? = today.sunProtection
}

class OverviewFeelsLike internal constructor(now: Hour) {
  val feelsLike: Temperature
  val feelsHotter: Boolean?

  init {
    feelsLike = now.feelsLike
    val noticeableDifference = when (now.feelsLike.unit) {
      TemperatureUnit.DegreeCelsius -> 1
      TemperatureUnit.DegreeFahrenheit -> 2
    }
    val difference = now.feelsLike.value - now.temperature.value
    feelsHotter = if (abs(difference) >= noticeableDifference) difference > 0 else null
  }
}

class OverviewWind internal constructor(now: Hour) {
  val speed: Speed = now.wind
  val direction: Angle = now.windDirection
  val maximumGust: Speed = now.gust
}

class OverviewHumidity internal constructor(now: Hour) {
  val relativeHumidity: Int = now.humidity
  val dewPoint: Temperature = now.dewPoint
}

class OverviewPressure internal constructor(now: Hour, today: Day) {
  val now: Pressure = now.pressure
  val average: Pressure = today.averagePressure
}