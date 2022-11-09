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

    val millimeters: Double = when (unit) {
        MILLIMETRE -> value
        INCH -> value * 25.4
        CENTIMETRE -> value * 10.0
    }
    val inches: Double = if (unit == INCH) value else millimeters / 25.4
    val centimeters: Double = if (unit == CENTIMETRE) value else millimeters / 10
}

enum class LengthUnit {
    MILLIMETRE, INCH, CENTIMETRE
}