package hr.dtakac.prognoza.shared.entity

class Length internal constructor(val metres: Double) {
    init {
        if (metres < 0) {
            throw IllegalStateException("Length must be >= 0, was $metres.")
        }
    }

    val millimetres: Double = metres * 1000
    val centimetres: Double = metres * 100
    val kilometres: Double = metres / 1000
    val inches: Double = metres * 39.3701
    val feet: Double = metres * 3.2808
    val miles: Double = metres * 0.0006214
}

enum class LengthUnit {
    Metric, Imperial
}