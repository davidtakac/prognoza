package hr.dtakac.prognoza.shared.entity

import kotlinx.datetime.*

class Forecast internal constructor(
  val timeZone: TimeZone,
  val days: List<Day>
) {
  val hours: List<Hour> by lazy { days.flatMap { it.hours } }

  /**
   * All future days including today.
   */
  val futureDays: List<Day>
    get() = days.filter {
      val dayDate = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone).date
      val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
      dayDate >= nowDate
    }

  /**
   * All past hours including the current one.
   */
  val pastHours: List<Hour> = hours - futureHours.drop(1).toSet()

  /**
   * All future hours including the current one.
   */
  val futureHours: List<Hour>
    get() = hours.filter {
      val hourDateTime = Instant.fromEpochSeconds(it.startUnixSecond).toLocalDateTime(timeZone)
      val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
      val nowDateTimeNormalized = LocalDateTime(
        year = nowDateTime.year,
        month = nowDateTime.month,
        dayOfMonth = nowDateTime.dayOfMonth,
        hour = nowDateTime.hour,
        minute = 0,
        second = 0,
        nanosecond = 0
      )
      hourDateTime >= nowDateTimeNormalized
    }

  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Forecast = Forecast(
    timeZone = timeZone,
    days = days.map { it.toMeasurementSystem(measurementSystem) }
  )
}

class Day internal constructor(
  val startUnixSecond: Long,
  val mostExtremeWmoCode: Int,
  val sunriseUnixSecond: Long?,
  val sunsetUnixSecond: Long?,
  val minimumTemperature: Temperature,
  val maximumTemperature: Temperature,
  val minimumFeelsLike: Temperature,
  val maximumFeelsLike: Temperature,
  val totalPrecipitation: Length,
  val totalRain: Length,
  val totalShowers: Length,
  val totalSnow: Length,
  val maximumPop: Int,
  val maximumWind: Speed,
  val maximumGust: Speed,
  val dominantWindDirection: Angle,
  val maximumUvIndex: UvIndex,
  val hours: List<Hour>
) {
  val representativeWmoCode: RepresentativeWmoCode by lazy {
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
  }

  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Day =
    Day(
      startUnixSecond = startUnixSecond,
      mostExtremeWmoCode = mostExtremeWmoCode,
      sunriseUnixSecond = sunriseUnixSecond,
      sunsetUnixSecond = sunsetUnixSecond,
      minimumTemperature = minimumTemperature.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
        else TemperatureUnit.DegreeCelsius
      ),
      maximumTemperature = maximumTemperature.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
        else TemperatureUnit.DegreeCelsius
      ),
      minimumFeelsLike = minimumFeelsLike.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
        else TemperatureUnit.DegreeCelsius
      ),
      maximumFeelsLike = maximumFeelsLike.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
        else TemperatureUnit.DegreeCelsius
      ),
      totalPrecipitation = totalPrecipitation.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
        else LengthUnit.Millimetre
      ),
      totalRain = totalRain.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
        else LengthUnit.Millimetre
      ),
      totalShowers = totalShowers.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
        else LengthUnit.Millimetre
      ),
      totalSnow = totalSnow.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
        else LengthUnit.Centimetre
      ),
      maximumPop = maximumPop,
      maximumWind = maximumWind.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) SpeedUnit.MilePerHour
        else SpeedUnit.KilometrePerHour
      ),
      maximumGust = maximumGust.convertTo(
        if (measurementSystem == MeasurementSystem.Imperial) SpeedUnit.MilePerHour
        else SpeedUnit.KilometrePerHour
      ),
      dominantWindDirection = dominantWindDirection,
      maximumUvIndex = maximumUvIndex,
      hours = hours.map { it.toMeasurementSystem(measurementSystem) }
    )
}

class RepresentativeWmoCode internal constructor(
  val wmoCode: Int,
  val isDay: Boolean
)

class Hour internal constructor(
  val startUnixSecond: Long,
  val wmoCode: Int,
  val temperature: Temperature,
  val rain: Length,
  val showers: Length,
  val snow: Length,
  val totalPrecipitation: Length,
  val pop: Int,
  val wind: Speed,
  val gust: Speed,
  val windDirection: Angle,
  val pressureAtSeaLevel: Pressure,
  val relativeHumidity: Int,
  val dewPoint: Temperature,
  val visibility: Length,
  val uvIndex: UvIndex,
  val isDay: Boolean,
  val feelsLike: Temperature
) {
  fun toMeasurementSystem(measurementSystem: MeasurementSystem): Hour = Hour(
    startUnixSecond = startUnixSecond,
    wmoCode = wmoCode,
    temperature = temperature.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
      else TemperatureUnit.DegreeCelsius
    ),
    rain = rain.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
      else LengthUnit.Millimetre
    ),
    showers = showers.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
      else LengthUnit.Millimetre
    ),
    snow = snow.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
      else LengthUnit.Centimetre
    ),
    totalPrecipitation = totalPrecipitation.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Inch
      else LengthUnit.Millimetre
    ),
    pop = pop,
    wind = wind.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) SpeedUnit.MilePerHour
      else SpeedUnit.KilometrePerHour
    ),
    gust = gust.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) SpeedUnit.MilePerHour
      else SpeedUnit.KilometrePerHour
    ),
    windDirection = windDirection,
    pressureAtSeaLevel = pressureAtSeaLevel.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) PressureUnit.InchOfMercury
      else PressureUnit.Millibar
    ),
    relativeHumidity = relativeHumidity,
    dewPoint = dewPoint.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
      else TemperatureUnit.DegreeCelsius
    ),
    visibility = visibility.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) LengthUnit.Mile
      else LengthUnit.Kilometre
    ),
    uvIndex = uvIndex,
    isDay = isDay,
    feelsLike = feelsLike.convertTo(
      if (measurementSystem == MeasurementSystem.Imperial) TemperatureUnit.DegreeFahrenheit
      else TemperatureUnit.DegreeCelsius
    )
  )
}