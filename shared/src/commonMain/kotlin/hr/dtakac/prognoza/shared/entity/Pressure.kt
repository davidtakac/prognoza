package hr.dtakac.prognoza.shared.entity

class Pressure(val millibars: Double) {
    init {
        if (millibars < 0) {
            throw IllegalStateException("Pressure must be >= 0, was $millibars.")
        }
    }

    val inchesOfMercury: Double = millibars / 33.8639
}

enum class PressureUnit {
    Millibar,
    InchOfMercury
}