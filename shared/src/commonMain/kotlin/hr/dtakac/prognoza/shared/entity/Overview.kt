package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.TimeZone

class Overview internal constructor(
  val timeZone: TimeZone,
  val now: OverviewNow,
  val hours: List<OverviewHour>,
  val days: OverviewDays,
  val precipitation: OverviewPrecipitation
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
        precipitation = buildPrecipitation(lastPeriodHours = last24Hours, futureDays = futureDays)
      )
    }

    private fun buildNow(now: Hour, today: Day) = OverviewNow(
      unixSecond = now.startUnixSecond,
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

    private fun buildPrecipitation(lastPeriodHours: List<Hour>, futureDays: List<Day>): OverviewPrecipitation {
      var nextExpectedRainStartUnixSecond: Long? = null
      var nextExpectedRain: Length? = null
      futureDays.firstOrNull { it.totalRain + it.totalShowers > Length.Zero }?.let { rainyDay ->
        nextExpectedRainStartUnixSecond = rainyDay.hours.firstOrNull { it.rain + it.showers > Length.Zero }?.startUnixSecond
        nextExpectedRain = (rainyDay.totalRain + rainyDay.totalShowers).takeIf { it > Length.Zero }?.convertTo(rainyDay.totalRain.unit)
      }

      var nextExpectedSnowStartUnixSecond: Long? = null
      var nextExpectedSnow: Length? = null
      futureDays.firstOrNull { it.totalSnow > Length.Zero }?.let { snowyDay ->
        nextExpectedSnowStartUnixSecond = snowyDay.hours.firstOrNull { it.snow > Length.Zero }?.startUnixSecond
        nextExpectedSnow = snowyDay.totalSnow.takeIf { it > Length.Zero }?.convertTo(snowyDay.totalSnow.unit)
      }

      // TODO: figure out why nextExpectedRain can be 0.1mm, but startUnixSecond can be null. Probably because
      //  the API returns an aggregate that doesn't necessarily correspond to the actual hourly values. To address this,
      //  you should probably calculate daily values directly from the hours.
      return OverviewPrecipitation(
        lastPeriodHourCount = lastPeriodHours.size,
        lastPeriodRain = lastPeriodHours.fold(Length.Zero) { acc, curr -> acc + curr.rain + curr.showers }.takeIf { it > Length.Zero },
        lastPeriodSnow = lastPeriodHours.fold(Length.Zero) { acc, curr -> acc + curr.snow }.takeIf { it > Length.Zero },
        nextExpectedRain = nextExpectedRain,
        nextExpectedRainStartUnixSecond = nextExpectedRainStartUnixSecond,
        nextExpectedSnow = nextExpectedSnow,
        nextExpectedSnowStartUnixSecond = nextExpectedSnowStartUnixSecond
      )
    }
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
  val lastPeriodRain: Length?,
  val lastPeriodSnow: Length?,
  val nextExpectedRain: Length?,
  val nextExpectedRainStartUnixSecond: Long?,
  val nextExpectedSnow: Length?,
  val nextExpectedSnowStartUnixSecond: Long?
)