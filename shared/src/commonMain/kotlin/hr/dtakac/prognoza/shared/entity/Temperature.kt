package hr.dtakac.prognoza.shared.entity

class Temperature internal constructor(val degreesCelsius: Double) : Comparable<Temperature> {
    init {
        if (degreesCelsius <= -273.15) {
            throw IllegalStateException("Temperature must be greater than -273.15 degrees Celsius, was $degreesCelsius.")
        }
    }

    val degreesFahrenheit: Double = degreesCelsius * 1.8 + 32

    override fun compareTo(other: Temperature): Int = degreesCelsius.compareTo(other.degreesCelsius)
}

enum class TemperatureUnit {
    DegreeCelsius, DegreeFahrenheit
}