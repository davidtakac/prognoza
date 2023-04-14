package hr.dtakac.prognoza.shared.entity

class Pressure(
    value: Double,
    unit: PressureUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Pressure must be >= 0, was $value.")
        }
    }

    val millibar: Double = when (unit) {
        PressureUnit.Millibar -> value
        PressureUnit.InchOfMercury -> value * 33.8639
    }
    val inchOfMercury: Double = if (unit == PressureUnit.InchOfMercury) value else millibar / 33.8639
}

enum class PressureUnit {
    Millibar,
    InchOfMercury
}