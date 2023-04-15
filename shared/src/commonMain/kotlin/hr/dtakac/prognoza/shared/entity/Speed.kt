package hr.dtakac.prognoza.shared.entity

class Speed(val metresPerSecond: Double) {
    init {
        if (this.metresPerSecond !in 0.0..299_792_458.0) {
            throw IllegalStateException("Speed must be >= 0 and <= c, was ${this.metresPerSecond}.")
        }
    }

    val kilometresPerHour: Double = metresPerSecond * 3.6
    val milesPerHour: Double = metresPerSecond * 2.2369
    val knots: Double = metresPerSecond * 1.9438
    val beaufortNumber: Int = when {
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

enum class SpeedUnit {
    MetrePerSecond,
    KilometrePerHour,
    MilePerHour,
    Knot,
    BeaufortNumber
}