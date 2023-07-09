package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone

class Overview internal constructor(
  val timeZone: TimeZone,
  val now: OverviewNow,
  val hours: List<OverviewHour>,
  val days: OverviewDays,
) {
  companion object {
    fun build(forecast: Forecast): Overview? {
      val hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
      val days = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
      return Overview(
        timeZone = forecast.timeZone,
        now = buildNow(now = hours[0], today = days[0]),
        hours = buildHours(hours = hours, days = days),
        days = buildDays(days = days),
      )
    }

    private fun buildNow(now: Hour, today: Day) = OverviewNow(
      unixSecond = now.startUnixSecond,
      temperature = now.temperature,
      minimumTemperature = today.minimumTemperature,
      maximumTemperature = today.maximumTemperature,
      feelsLike = now.feelsLike,
      wmoCode = now.wmoCode,
      isDay = now.isDay,
      totalPrecipitation = now.totalPrecipitation,
      snowFall = now.snow,
      rainFall = (now.rain + now.showers),
      pressure = now.pressureAtSeaLevel,
      uvIndex = now.uvIndex,
      wind = now.wind,
      windDirection = now.windDirection,
      gust = now.gust,
      humidity = now.relativeHumidity,
      dewPoint = now.dewPoint,
      visibility = now.visibility
    )

    private fun buildHours(hours: List<Hour>, days: List<Day>) = buildList<OverviewHour> {
      val overviewHours = hours.map {
        OverviewHour.Weather(
          unixSecond = it.startUnixSecond,
          temperature = it.temperature,
          pop = it.pop,
          wmoCode = it.wmoCode,
          isDay = it.isDay
        )
      }
      addAll(overviewHours)

      val sunrises = days
        .mapNotNull { it.sunriseUnixSecond }
        .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
        .map { OverviewHour.Sunrise(it) }
      addAll(sunrises)

      val sunsets = days
        .mapNotNull { it.sunsetUnixSecond }
        .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
        .map { OverviewHour.Sunset(it) }
      addAll(sunsets)

      // Ensures sunrises and sunsets are placed in between Weather hours
      sortBy { it.unixSecond }
    }

    private fun buildDays(days: List<Day>) = OverviewDays(
      days = days.map { day ->
        OverviewDay(
          unixSecond = day.startUnixSecond,
          representativeWmoCode = day.representativeWmoCode.wmoCode,
          representativeWmoCodeIsDay = day.representativeWmoCode.isDay,
          minimumTemperature = day.minimumTemperature,
          maximumTemperature = day.maximumTemperature,
          maximumPop = day.maximumPop,
        )
      },
      minimumTemperature = days.minOf { it.minimumTemperature },
      maximumTemperature = days.maxOf { it.maximumTemperature }
    )
  }
}

class OverviewNow internal constructor(
  val unixSecond: Long,
  val temperature: Temperature,
  val minimumTemperature: Temperature,
  val maximumTemperature: Temperature,
  val feelsLike: Temperature,
  val wmoCode: Int,
  val isDay: Boolean,
  val totalPrecipitation: Length,
  val snowFall: Length,
  val rainFall: Length,
  val pressure: Pressure,
  val uvIndex: UvIndex,
  val wind: Speed,
  val gust: Speed,
  val windDirection: Angle,
  val humidity: Int,
  val dewPoint: Temperature,
  val visibility: Length
)

sealed interface OverviewHour {
  val unixSecond: Long

  class Weather internal constructor(
    override val unixSecond: Long,
    val temperature: Temperature,
    val pop: Int,
    val wmoCode: Int,
    val isDay: Boolean
  ) : OverviewHour

  class Sunrise internal constructor(override val unixSecond: Long) : OverviewHour

  class Sunset internal constructor(override val unixSecond: Long) : OverviewHour
}

class OverviewDays internal constructor(
  val days: List<OverviewDay>,
  val minimumTemperature: Temperature,
  val maximumTemperature: Temperature
)

class OverviewDay internal constructor(
  val unixSecond: Long,
  val representativeWmoCode: Int,
  val representativeWmoCodeIsDay: Boolean,
  val minimumTemperature: Temperature,
  val maximumTemperature: Temperature,
  val maximumPop: Int
)