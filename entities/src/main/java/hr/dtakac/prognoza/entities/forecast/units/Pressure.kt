package hr.dtakac.prognoza.entities.forecast.units

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
        PressureUnit.MILLIBAR -> value
        PressureUnit.INCH_OF_MERCURY -> value * 33.8639
    }
    val inchesOfMercury: Double = if (unit == PressureUnit.INCH_OF_MERCURY) value else millibar / 33.8639
}

enum class PressureUnit {
    MILLIBAR,
    INCH_OF_MERCURY
}