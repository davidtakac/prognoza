package hr.dtakac.prognoza.shared.entity

class Percentage internal constructor(val percent: Double) {
    init {
        if (percent !in 0.0..100.0) {
            throw IllegalStateException("Value in percent must be in [0, 100], was $percent.")
        }
    }

    val fraction: Double = percent / 100
}