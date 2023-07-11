package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.math.abs
import kotlin.reflect.KProperty1
import kotlin.time.Duration.Companion.hours

class Overview internal constructor(forecast: Forecast) {
  val timeZone: TimeZone = forecast.timeZone
  val now: OverviewNow = OverviewNow(forecast.now, forecast.today)
  val hours: List<OverviewHour> = buildHours(forecast)
  val days: OverviewDays = OverviewDays(forecast.fromToday)
  val rainfall: OverviewPrecipitation = OverviewPrecipitation(forecast, Hour::rainAndShowers, Day::totalRainAndShowers)
  val snowfall: OverviewPrecipitation? = OverviewPrecipitation(forecast, Hour::snow, Day::totalSnow)
    .takeUnless { it.past.amount.value == 0.0 && it.future is OverviewPrecipitation.Future.NoneExpected }
  val uvIndex: OverviewUvIndex = OverviewUvIndex(forecast.now, forecast.today)
  val feelsLike: OverviewFeelsLike = OverviewFeelsLike(forecast.now)

  private fun buildHours(forecast: Forecast) = buildList<OverviewHour> {
    val hours = forecast.fromToday.flatMap { it.fromNow }.take(24)
    addAll(hours.map { OverviewHour.Weather(it) })

    val fromToday = forecast.fromToday
    val sunrises = fromToday
      .mapNotNull { it.sunriseUnixSecond }
      .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
      .map { OverviewHour.Sunrise(it) }
    addAll(sunrises)

    val sunsets = fromToday
      .mapNotNull { it.sunsetUnixSecond }
      .filter { it in hours.first().startUnixSecond..hours.last().startUnixSecond }
      .map { OverviewHour.Sunset(it) }
    addAll(sunsets)

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
  val representativeWmoCode: Int = day.representativeWmoCode.wmoCode
  val representativeWmoCodeIsDay: Boolean = day.representativeWmoCode.isDay
  val minimumTemperature: Temperature = day.minimumTemperature
  val maximumTemperature: Temperature = day.maximumTemperature
  val maximumPop: Int = day.maximumPop.humanValue
}

class OverviewPrecipitation internal constructor(
  forecast: Forecast,
  hourlyPrecipitation: KProperty1<Hour, Length>,
  dailyPrecipitation: KProperty1<Day, Length>
) {
  val past: Past
  val future: Future

  init {
    val unit = hourlyPrecipitation.get(forecast.now).unit
    past = (forecast.today.untilNow - forecast.now).let {
      Past(
        hours = it.size,
        amount = it.fold(Length(0.0, unit)) { acc, hour -> acc + hourlyPrecipitation.get(hour) }
      )
    }

    val futureToday = forecast.today.fromNow.let { fromNow ->
      var firstRainyIdx: Int? = null
      var lastRainyIdx: Int? = null
      var todayAmount = Length(0.0, unit)
      for (i in fromNow.indices) {
        val curr = fromNow[i]
        if (hourlyPrecipitation.get(curr).value > 0) {
          if (firstRainyIdx == null) firstRainyIdx = i else lastRainyIdx = i
          todayAmount += hourlyPrecipitation.get(curr)
        } else if (lastRainyIdx != null && i - lastRainyIdx >= 2) {
          break
        }
      }

      val todayStartUnixSecond = firstRainyIdx?.let { fromNow[it].startUnixSecond }
      val todayEndUnixSecond = lastRainyIdx?.let { fromNow[it].startUnixSecond }

      if (todayStartUnixSecond == null) {
        if (todayEndUnixSecond == null) {
          null
        } else {
          Future.Today.WillEnd(
            endUnixSecond = todayEndUnixSecond,
            amount = todayAmount
          )
        }
      } else {
        if (todayEndUnixSecond == null) {
          Future.Today.WillStart(
            startUnixSecond = todayStartUnixSecond,
            amount = todayAmount
          )
        } else {
          Future.Today.WillStartThenEnd(
            startUnixSecond = todayStartUnixSecond,
            endUnixSecond = todayEndUnixSecond,
            amount = todayAmount
          )
        }
      }
    }


    val futureDayOfWeek =
      (forecast.fromToday - forecast.today).firstOrNull { dailyPrecipitation.get(it).value > 0 }?.let {
        Future.DayOfWeek(
          startUnixSecond = it.startUnixSecond,
          amount = dailyPrecipitation.get(it)
        )
      }

    future = futureToday ?: futureDayOfWeek ?: Future.NoneExpected(days = forecast.fromToday.size)
  }

  class Past internal constructor(
    val hours: Int,
    val amount: Length
  )

  sealed interface Future {
    sealed interface Today : Future {
      class WillStartThenEnd internal constructor(
        val startUnixSecond: Long,
        val endUnixSecond: Long,
        val amount: Length
      ) : Today

      class WillStart internal constructor(
        val startUnixSecond: Long,
        val amount: Length
      ) : Today

      class WillEnd internal constructor(
        val endUnixSecond: Long,
        val amount: Length
      ) : Today
    }

    class DayOfWeek internal constructor(
      val startUnixSecond: Long,
      val amount: Length
    ) : Future

    class NoneExpected internal constructor(val days: Int) : Future
  }
}

class OverviewUvIndex internal constructor(now: Hour, today: Day) {
  val uvIndex: UvIndex
  val protection: Protection

  sealed interface Protection {
    object None : Protection
    data class WillStart(val startUnixSecond: Long) : Protection
    data class WillEnd(val endUnixSecond: Long) : Protection
    data class WillStartAndEnd(val startUnixSecond: Long, val endUnixSecond: Long) : Protection
  }

  init {
    uvIndex = now.uvIndex
    val firstDangerousHourStartUnixSecond = today.hours
      .firstOrNull { it.uvIndex.protectionNeeded }?.startUnixSecond
      // If we're past the first hour, don't display the start because it's noise
      ?.takeUnless { (Clock.System.now().epochSeconds - it) > 1.hours.inWholeSeconds }
    val lastDangerousHourStartUnixSecond = today.hours
      .lastOrNull { it.uvIndex.protectionNeeded }?.startUnixSecond
      // If we're past the last hour, don't display end because it's also noise
      ?.takeUnless { (Clock.System.now().epochSeconds - it) > 1.hours.inWholeSeconds }

    protection = if (firstDangerousHourStartUnixSecond == null) {
      if (lastDangerousHourStartUnixSecond == null) Protection.None
      else Protection.WillEnd(lastDangerousHourStartUnixSecond)
    } else {
      if (lastDangerousHourStartUnixSecond == null) Protection.WillStart(firstDangerousHourStartUnixSecond)
      else Protection.WillStartAndEnd(firstDangerousHourStartUnixSecond, lastDangerousHourStartUnixSecond)
    }
  }
}

class OverviewFeelsLike internal constructor(now: Hour) {
  val feelsLike: Temperature

  /**
   *  - true when feels like temperature is higher because of humidity,
   *  - false when it is lower because of wind, and
   *  - null when it is comparable to the actual temperature.
   */
  val higherThanActualTemperature: Boolean?

  init {
    val noticeableDifference = when (now.feelsLike.unit) {
      TemperatureUnit.DegreeCelsius -> 1
      TemperatureUnit.DegreeFahrenheit -> 2
    }
    val difference = now.feelsLike.value - now.temperature.value
    higherThanActualTemperature = if (abs(difference) >= noticeableDifference) {
      difference > 0
    } else null
    feelsLike = now.feelsLike
  }
}