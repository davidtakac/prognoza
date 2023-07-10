package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.math.abs
import kotlin.time.Duration.Companion.hours

class Overview internal constructor(
  val timeZone: TimeZone,
  val now: OverviewNow,
  val hours: List<OverviewHour>,
  val days: OverviewDays,
  val rainfall: OverviewPrecipitation,
  val snowfall: OverviewPrecipitation?,
  val uvIndex: OverviewUvIndex,
  val feelsLike: OverviewFeelsLike
) {
  // todo: why build? use a constructor like god intended :D Go by uv index and feelslike. Do the
  //  same for Forecast, units of measure, etc.
  companion object {
    fun build(forecast: Forecast): Overview? {
      val last24Hours = forecast.pastHours.take(24)
      val next24Hours = forecast.futureHours.take(24).takeIf { it.isNotEmpty() } ?: return null
      val futureDays = forecast.futureDays.takeIf { it.isNotEmpty() } ?: return null
      return Overview(
        timeZone = forecast.timeZone,
        now = buildNow(next24Hours[0], futureDays[0]),
        hours = buildHours(next24Hours, futureDays),
        days = buildDays(futureDays),
        rainfall = buildRainfall(last24Hours, futureDays),
        snowfall = buildSnowfall(last24Hours, futureDays)
          .takeIf { it.amountInLastPeriod.value > 0 || it.startUnixSecondOfNextExpectedAmount != null },
        uvIndex = OverviewUvIndex(futureDays[0].hours),
        feelsLike = OverviewFeelsLike(next24Hours[0])
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
          pop = it.pop.humanValue,
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
          maximumPop = day.maximumPop.humanValue,
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
        hoursInLastPeriod = lastPeriodHours.size,
        amountInLastPeriod = lastPeriodHours.fold(Length(0.0, unit)) { acc, curr -> acc + curr.rain + curr.showers },
        startUnixSecondOfNextExpectedAmount = nextExpectedStartUnixSecond,
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
        hoursInLastPeriod = lastPeriodHours.size,
        amountInLastPeriod = lastPeriodHours.fold(Length(0.0, unit)) { acc, curr -> acc + curr.snow },
        startUnixSecondOfNextExpectedAmount = nextExpectedStartUnixSecond,
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
  val hoursInLastPeriod: Int,
  val amountInLastPeriod: Length,
  val startUnixSecondOfNextExpectedAmount: Long?,
  val nextExpectedAmount: Length
)

class OverviewUvIndex internal constructor(hours: List<Hour>) {
  val uvIndex: UvIndex
  val protectionStartUnixSecond: Long?
  val protectionEndUnixSecond: Long?

  init {
    uvIndex = hours[0].uvIndex
    protectionStartUnixSecond = hours.firstOrNull { it.uvIndex.protectionNeeded }
      // Do not display a redundant start time if we're already in the danger zone
      ?.startUnixSecond?.takeUnless { (Clock.System.now().epochSeconds - it) > 1.hours.inWholeSeconds }
    protectionEndUnixSecond = hours.lastOrNull { it.uvIndex.protectionNeeded }
      // Do not display a redundant end time if we're out of the danger zone
      ?.startUnixSecond?.takeUnless { (Clock.System.now().epochSeconds - it) > 1.hours.inWholeSeconds }
  }
}

class OverviewFeelsLike internal constructor(hour: Hour) {
  val feelsLike: Temperature
  /**
   *  - true when feels like temperature is higher because of humidity,
   *  - false when it is lower because of wind, and
   *  - null when it is comparable to the actual temperature.
   */
  val higherThanActualTemperature: Boolean?

  init {
    val noticeableDifference = when (hour.feelsLike.unit) {
      TemperatureUnit.DegreeCelsius -> 1
      TemperatureUnit.DegreeFahrenheit -> 2
    }
    val difference = hour.feelsLike.value - hour.temperature.value
    higherThanActualTemperature = if (abs(difference) >= noticeableDifference) { difference > 0 } else null
    feelsLike = hour.feelsLike
  }
}