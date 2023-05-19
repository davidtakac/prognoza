package hr.dtakac.prognoza.shared.entity

class Temperature internal constructor(
    val value: Double,
    val unit: TemperatureUnit
) : Comparable<Temperature> {
    init {
        if (valueIn(TemperatureUnit.Kelvin) < 0.0) throwTemperatureBelowAbsoluteZero()
    }

    override fun compareTo(other: Temperature): Int {
        val thisCelsius = valueIn(TemperatureUnit.DegreeCelsius)
        val otherCelsius = other.valueIn(TemperatureUnit.DegreeCelsius)
        return thisCelsius.compareTo(otherCelsius)
    }

    override fun toString(): String = "$value ${unit.suffix}"

    internal fun convertTo(targetUnit: TemperatureUnit) = Temperature(valueIn(targetUnit), targetUnit)

    private fun valueIn(targetUnit: TemperatureUnit): Double =
        if (unit == targetUnit) value
        else when (unit) {
            TemperatureUnit.Kelvin -> when (targetUnit) {
                TemperatureUnit.DegreeCelsius -> kelvinToCelsius(value)
                TemperatureUnit.DegreeFahrenheit -> kelvinToFahrenheit(value)
                TemperatureUnit.Kelvin -> value
            }
            TemperatureUnit.DegreeCelsius -> when (targetUnit) {
                TemperatureUnit.Kelvin -> celsiusToKelvin(value)
                TemperatureUnit.DegreeFahrenheit -> celsiusToFahrenheit(value)
                TemperatureUnit.DegreeCelsius -> value
            }
            TemperatureUnit.DegreeFahrenheit -> when (targetUnit) {
                TemperatureUnit.Kelvin -> fahrenheitToKelvin(value)
                TemperatureUnit.DegreeCelsius -> fahrenheitToCelsius(value)
                TemperatureUnit.DegreeFahrenheit -> value
            }
        }

    private fun kelvinToCelsius(kelvin: Double) = kelvin - 273.15

    private fun kelvinToFahrenheit(kelvin: Double) = kelvin * 9 / 5 - 459.67

    private fun celsiusToKelvin(celsius: Double) = celsius + 273.15

    private fun celsiusToFahrenheit(celsius: Double) = celsius * 9 / 5 + 32

    private fun fahrenheitToKelvin(fahrenheit: Double) = (fahrenheit + 459.67) * 5 / 9

    private fun fahrenheitToCelsius(fahrenheit: Double) = (fahrenheit - 32) * 5 / 9

    private fun throwTemperatureBelowAbsoluteZero(): Nothing =
        throw IllegalStateException("Temperature must be greater than 0K, was ${toString()}.")
}

enum class TemperatureUnit(internal val suffix: String) {
    Kelvin(suffix = "K"),
    DegreeCelsius(suffix = "°C"),
    DegreeFahrenheit(suffix = "°F")
}