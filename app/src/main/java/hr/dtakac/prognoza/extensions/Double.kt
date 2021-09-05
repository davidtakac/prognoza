package hr.dtakac.prognoza.extensions

import hr.dtakac.prognoza.R
import kotlin.math.roundToInt

fun Double.toCompassDirection() = when (this.roundToInt()) {
    in 0..44 -> R.string.direction_n
    in 45..89 -> R.string.direction_ne
    in 90..134 -> R.string.direction_e
    in 135..179 -> R.string.direction_se
    in 180..224 -> R.string.direction_s
    in 225..269 -> R.string.direction_sw
    in 270..314 -> R.string.direction_w
    else -> R.string.direction_n
}

fun Double.millimetresToInches() = this * 0.03937
fun Double.metersPerSecondToKilometresPerHour() = this * 3.6
fun Double.metersPerSecondToMilesPerHour() = this * 2.2369
fun Double.degreesCelsiusToDegreesFahrenheit() = this * 1.8 + 32
fun Double.hectoPascalToPsi() = this * 0.01450
fun Double.degreesFahrenheitToDegreesCelsius() = (this - 32) / 1.8