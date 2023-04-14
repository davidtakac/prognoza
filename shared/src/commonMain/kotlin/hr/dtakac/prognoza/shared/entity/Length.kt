package hr.dtakac.prognoza.shared.entity

import hr.dtakac.prognoza.shared.entity.LengthUnit.*

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
        Millimetre -> value
        Inch -> value * 25.4
        Centimetre -> value * 10.0
    }
    val inch: Double = if (unit == Inch) value else millimetre / 25.4
    val centimetre: Double = if (unit == Centimetre) value else millimetre / 10
}

enum class LengthUnit {
    Millimetre, Inch, Centimetre
}