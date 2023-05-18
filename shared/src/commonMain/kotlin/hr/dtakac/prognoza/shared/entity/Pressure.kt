package hr.dtakac.prognoza.shared.entity

class Pressure internal constructor(
    val value: Double,
    val unit: PressureUnit
) {
    init {
        if (value < 0) throwInvalidPressure()
    }

    override fun toString(): String = "$value ${unit.suffix}"

    fun convertTo(targetUnit: PressureUnit): Pressure = Pressure(valueIn(targetUnit), targetUnit)

    private fun valueIn(targetUnit: PressureUnit): Double =
        if (targetUnit == unit) value
        else value * unit.pascals / targetUnit.pascals

    private fun throwInvalidPressure(): Nothing =
        throw IllegalStateException("Pressure must be positive, was ${toString()}.")
}

enum class PressureUnit(
    internal val pascals: Double,
    internal val suffix: String
) {
    Pascal(pascals = 1.0, suffix = "Pa"),
    Millibar(pascals = 100.0, suffix = "mbar"),
    InchOfMercury(pascals = 3386.39, suffix = "inHg")
}