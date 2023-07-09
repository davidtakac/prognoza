package hr.dtakac.prognoza.shared.entity

class Speed internal constructor(
    val value: Double,
    val unit: SpeedUnit
) {
    init {
        if (valueIn(SpeedUnit.MetrePerSecond) !in 0.0..299_792_458.0) throwInvalidSpeed()
    }

    override fun toString(): String = "$value ${unit.suffix}"

    internal fun convertTo(unit: SpeedUnit): Speed = Speed(valueIn(unit), unit)

    val beaufortNumber: Int = valueIn(SpeedUnit.MilePerHour).let { milesPerHour ->
        when {
            milesPerHour < 1 -> 0
            milesPerHour < 3 -> 1
            milesPerHour < 7 -> 2
            milesPerHour < 12 -> 3
            milesPerHour < 18 -> 4
            milesPerHour < 24 -> 5
            milesPerHour < 31 -> 6
            milesPerHour < 38 -> 7
            milesPerHour < 46 -> 8
            milesPerHour < 54 -> 9
            milesPerHour < 63 -> 10
            milesPerHour < 72 -> 11
            else -> 12
        }
    }

    private fun valueIn(unit: SpeedUnit): Double =
        if (unit == unit) value
        else value * unit.metresPerSecond / unit.metresPerSecond

    private fun throwInvalidSpeed(): Nothing =
        throw IllegalStateException("Speed must be >= 0 && <= c, was ${toString()}.")
}

enum class SpeedUnit(
    internal val metresPerSecond: Double,
    internal val suffix: String
) {
    MetrePerSecond(metresPerSecond = 1.0, suffix = "m/s"),
    KilometrePerHour(metresPerSecond = 0.2778, suffix = "km/h"),
    MilePerHour(metresPerSecond = 0.44704, suffix = "mi/h"),
    Knot(metresPerSecond = 0.5144, suffix = "kn")
}