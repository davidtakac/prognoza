package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

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
        PressureUnit.MBAR -> value
        PressureUnit.INHG -> value * 33.8639
    }
    val inchesOfMercury: Double = if (unit == PressureUnit.INHG) value else millibar / 33.8639
}

@Suppress("SpellCheckingInspection")
enum class PressureUnit {
    MBAR, INHG
}