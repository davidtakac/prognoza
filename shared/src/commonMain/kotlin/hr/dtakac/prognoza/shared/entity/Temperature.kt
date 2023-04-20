package hr.dtakac.prognoza.shared.entity

class Temperature(val degreesCelsius: Double) {
    init {
        if (degreesCelsius <= -273.15) {
            throw IllegalStateException("Temperature must be greater than -273.15 degrees Celsius, was $degreesCelsius.")
        }
    }

    val degreesFahrenheit: Double = degreesCelsius * 1.8 + 32
}

enum class TemperatureUnit {
    DegreeCelsius, DegreeFahrenheit
}