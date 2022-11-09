package hr.dtakac.prognoza.entities.forecast.units

import hr.dtakac.prognoza.entities.forecast.units.LengthUnit.*

class Length(
    value: Double,
    unit: LengthUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Length must be >= 0, was $value.")
        }
    }

    val millimetre: Double = when (unit) {
        MILLIMETRE -> value
        INCH -> value * 25.4
        CENTIMETRE -> value * 10.0
    }
    val inch: Double = if (unit == INCH) value else millimetre / 25.4
    val centimetre: Double = if (unit == CENTIMETRE) value else millimetre / 10
}

enum class LengthUnit {
    MILLIMETRE, INCH, CENTIMETRE
}