package hr.dtakac.prognoza.shared.entity

class Hour internal constructor(
  val startUnixSecond: Long,
  val wmoCode: Int,
  val temperature: Temperature,
  val rain: Length,
  val showers: Length,
  val rainAndShowers: Length = rain + showers,
  val snow: Length,
  val pop: Pop,
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