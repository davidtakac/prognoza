package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone

class Overview internal constructor(
  val timeZone: TimeZone,
  val now: OverviewNow,
  val hours: List<OverviewHour>,
  val days: OverviewDays,
  val rainfall: OverviewPrecipitation,
  val snowfall: OverviewPrecipitation?
) {
  companion object {
    fun build(forecast: Forecast): Overview? {
      val last24Hours = forecast.pastHours.take(24)
      val next24Hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
      val futureDays = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
      return Overview(
        timeZone = forecast.timeZone,
        now = buildNow(now = next24Hours[0], today = futureDays[0]),
        hours = buildHours(hours = next24Hours, days = futureDays),
        days = buildDays(days = futureDays),
        rainfall = buildRainfall(lastPeriodHours = last24Hours, futureDays = futureDays),
        snowfall = buildSnowfall(
          lastPeriodHours = last24Hours,
          futureDays = futureDays
        ).takeIf { it.lastPeriodAmount.value > 0 || it.nextExpectedStartUnixSecond != null },
      )
    }

    private fun buildNow(now: Hour, today: Day) = OverviewNow(
      temperature = now.temperature,
      minimumTemperature = today.minimumTemperature,
      maximumTemperature = today.maximumTemperature,
      feelsLike = now.feelsLike,
      wmoCode = now.wmoCode,
      isDay = now.isDay
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

    private fun buildRainfall(
      lastPeriodHours: List<Hour>,
      futureDays: List<Day>
    ): OverviewPrecipitation {
      val unit = lastPeriodHours[0].rain.unit
      var nextExpectedStartUnixSecond: Long? = null
      var nextExpectedAmount = Length(0.0, unit)
      futureDays.firstOrNull { (it.totalRain + it.totalShowers).value > 0 }?.let { rainyDay ->
        nextExpectedStartUnixSecond =
          rainyDay.hours.firstOrNull { (it.rain + it.showers).value > 0 }?.startUnixSecond
        nextExpectedAmount = (rainyDay.totalRain + rainyDay.totalShowers)
      }
      return OverviewPrecipitation(
        lastPeriodHourCount = lastPeriodHours.size,
        lastPeriodAmount = lastPeriodHours.fold(
          Length(
            value = 0.0,
            unit = lastPeriodHours[0].rain.unit
          )
        ) { acc, curr -> acc + curr.rain + curr.showers },
        nextExpectedStartUnixSecond = nextExpectedStartUnixSecond,
        nextExpectedAmount = nextExpectedAmount
      )
    }

    private fun buildSnowfall(
      lastPeriodHours: List<Hour>,
      futureDays: List<Day>
    ): OverviewPrecipitation {
      val unit = lastPeriodHours[0].snow.unit
      var nextExpectedStartUnixSecond: Long? = null
      var nextExpectedAmount = Length(0.0, unit)
      futureDays.firstOrNull { it.totalSnow.value > 0 }?.let { snowyDay ->
        nextExpectedStartUnixSecond = snowyDay.hours.firstOrNull { it.snow.value > 0 }?.startUnixSecond
        nextExpectedAmount = snowyDay.totalSnow
      }
      return OverviewPrecipitation(
        lastPeriodHourCount = lastPeriodHours.size,
        lastPeriodAmount = lastPeriodHours.fold(
          Length(
            0.0,
            LengthUnit.Centimetre
          )
        ) { acc, curr -> acc + curr.snow },
        nextExpectedStartUnixSecond = nextExpectedStartUnixSecond,
        nextExpectedAmount = nextExpectedAmount
      )
    }
  }
}

class OverviewNow internal constructor(
  val temperature: Temperature,
  val minimumTemperature: Temperature,
  val maximumTemperature: Temperature,
  val feelsLike: Temperature,
  val wmoCode: Int,
  val isDay: Boolean,
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

class OverviewPrecipitation internal constructor(
  val lastPeriodHourCount: Int,
  val lastPeriodAmount: Length,
  val nextExpectedStartUnixSecond: Long?,
  val nextExpectedAmount: Length
)