package hr.dtakac.prognoza.shared.entity

class Speed(
    value: Double,
    unit: SpeedUnit
) {
    init {
        if (value < 0) {
            throw IllegalStateException("Speed must be >= 0, was $value.")
        }
    }

    val metrePerSecond: Double = when (unit) {
        SpeedUnit.MetrePerSecond -> value
        SpeedUnit.KilometrePerHour -> value / 3.6
        SpeedUnit.MilePerHour -> value / 2.2369
        SpeedUnit.Knot -> value / 1.9438
    }
    val kilometrePerHour: Double = if (unit == SpeedUnit.KilometrePerHour) value else metrePerSecond * 3.6
    val milePerHour: Double = if (unit == SpeedUnit.MilePerHour) value else metrePerSecond * 2.2369
    val knot: Double = if (unit == SpeedUnit.Knot) value else metrePerSecond * 1.9438

    // Beaufort scale: https://www.weather.gov/mfl/beaufort
    val beaufortScale: Byte = when {
        milePerHour < 1 -> 0
        milePerHour < 3 -> 1
        milePerHour < 7 -> 2
        milePerHour < 12 -> 3
        milePerHour < 18 -> 4
        milePerHour < 24 -> 5
        milePerHour < 31 -> 6
        milePerHour < 38 -> 7
        milePerHour < 46 -> 8
        milePerHour < 54 -> 9
        milePerHour < 63 -> 10
        milePerHour < 72 -> 11
        else -> 12
    }
}

enum class SpeedUnit {
    MetrePerSecond,
    KilometrePerHour,
    MilePerHour,
    Knot
}