package hr.dtakac.prognoza.shared.entity

class Temperature internal constructor(
  val value: Double,
  val unit: TemperatureUnit
) : Comparable<Temperature> {
  init {
    if (valueIn(TemperatureUnit.DegreeCelsius) < -273.15) throwTemperatureBelowAbsoluteZero()
  }

  override fun compareTo(other: Temperature): Int {
    val thisCelsius = valueIn(TemperatureUnit.DegreeCelsius)
    val otherCelsius = other.valueIn(TemperatureUnit.DegreeCelsius)
    return thisCelsius.compareTo(otherCelsius)
  }

  override fun toString(): String = "$value ${unit.suffix}"

  internal fun convertTo(unit: TemperatureUnit) = Temperature(valueIn(unit), unit)

  private fun valueIn(unit: TemperatureUnit): Double =
    if (unit == this.unit) value
    else when (this.unit) {
      TemperatureUnit.DegreeCelsius -> when (unit) {
        TemperatureUnit.DegreeFahrenheit -> celsiusToFahrenheit(value)
        TemperatureUnit.DegreeCelsius -> value
      }
      TemperatureUnit.DegreeFahrenheit -> when (unit) {
        TemperatureUnit.DegreeCelsius -> fahrenheitToCelsius(value)
        TemperatureUnit.DegreeFahrenheit -> value
      }
    }

  private fun celsiusToFahrenheit(celsius: Double) = celsius * 9 / 5 + 32

  private fun fahrenheitToCelsius(fahrenheit: Double) = (fahrenheit - 32) * 5 / 9

  private fun throwTemperatureBelowAbsoluteZero(): Nothing =
    throw IllegalStateException("Temperature must be greater than -273.15, was ${toString()}.")
}

enum class TemperatureUnit(internal val suffix: String) {
  DegreeCelsius(suffix = "°C"),
  DegreeFahrenheit(suffix = "°F")
}