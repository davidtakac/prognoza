package hr.dtakac.prognoza.entities.forecast.units

import java.lang.IllegalStateException

class Pressure(
    private val value: Double,
    private val unit: PressureUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Pressure must be >= 0, was $value.")
        }
    }

    val hectoPascal: Double = when (unit) {
        PressureUnit.HPA -> value
        PressureUnit.PSI -> value * 6894.76
        PressureUnit.BAR -> value * 1000
        PressureUnit.ATM -> value * 1013.25
    }
    val poundsPerSquareInch: Double = if (unit == PressureUnit.PSI) value else hectoPascal / 68.9475728
    val bar: Double = if (unit == PressureUnit.BAR) value else hectoPascal / 1000
    val atm: Double = if (unit == PressureUnit.ATM) value else hectoPascal / 1013
}

enum class PressureUnit {
    HPA, PSI, BAR, ATM
}