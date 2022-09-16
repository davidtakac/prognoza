package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException
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

    val millimeters: Double = if (unit == MM) value else value * 25.4
    val inches: Double = if (unit == IN) value else millimeters * 0.03937
}

enum class LengthUnit {
    MM, IN
}