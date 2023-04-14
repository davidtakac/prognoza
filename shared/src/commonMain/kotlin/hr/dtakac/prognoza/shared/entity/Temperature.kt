package hr.dtakac.prognoza.shared.entity

class Temperature(
    value: Double,
    unit: TemperatureUnit
) : Comparable<Temperature> {
    init {
        val celsius = calculateCelsius(value, unit)
        if (celsius <= -273.15) {
            throw IllegalStateException("Temperature must be greater than -273.15 degrees Celsius, was $celsius.")
        }
    }

    val celsius: Double = calculateCelsius(value, unit)
    val fahrenheit: Double = if (unit == TemperatureUnit.DegreeFahrenheit) value else celsius * 1.8 + 32

    override fun compareTo(other: Temperature): Int {
        return celsius.compareTo(other.celsius)
    }

    private fun calculateCelsius(
        value: Double,
        unit: TemperatureUnit
    ): Double = if (unit == TemperatureUnit.DegreeCelsius) value else (value - 32) / 1.8
}

enum class TemperatureUnit {
    DegreeCelsius,
    DegreeFahrenheit
}