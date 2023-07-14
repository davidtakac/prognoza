package hr.dtakac.prognoza.shared.entity

class Day internal constructor(val hours: List<Hour>) {
  init {
    if (hours.isEmpty()) throwInvalidHours()
  }

  val startUnixSecond: Long = hours.first().startUnixSecond
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
  val averagePressure: Pressure = hours.fold(Pressure(0.0, hours[0].pressure.unit)) { acc, hour -> acc + hour.pressure } / hours.size
  val representativeWmoCode: RepresentativeWmoCode = RepresentativeWmoCode(hours)
  val sunProtection: SunProtection? = hours
    .firstOrNull { it.uvIndex.isDangerous }
    ?.let { firstDangerousHour ->
      SunProtection(
        fromUnixSecond = firstDangerousHour.startUnixSecond,
        untilUnixSecond = hours.last { it.uvIndex.isDangerous }.startUnixSecond
      )
    }

  private fun throwInvalidHours(): Nothing {
    throw IllegalStateException("Hours must not be empty.")
  }
}

class RepresentativeWmoCode internal constructor(hours: List<Hour>) {
  val wmoCode: Int
  val isDay: Boolean

  init {
    if (hours.any { it.wmoCode > 48 }) {
      // The most severe weather condition is at least light drizzle. Because this involves
      // precipitation, it could affect people's plans for the day. In this case they would
      // likely want to know the most severe weather condition so they can prepare accordingly
      val mostExtremeHour = hours.maxBy { it.wmoCode }
      wmoCode = mostExtremeHour.wmoCode
      isDay = mostExtremeHour.isDay
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
      wmoCode = mostCommonWmoCode
      isDay = priorityHours[0].isDay
    }
  }
}

class SunProtection internal constructor(
  val fromUnixSecond: Long,
  val untilUnixSecond: Long
)