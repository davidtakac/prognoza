package hr.dtakac.prognoza.extensions

import hr.dtakac.prognoza.R
import kotlin.math.roundToInt

fun Float?.isPrecipitationAmountSignificant() = this != null && this >= 0.1f

fun Float.toCompassDirection() = when (this.roundToInt()) {
    in 0..44 -> R.string.direction_n
    in 45..89 -> R.string.direction_ne
    in 90..134 -> R.string.direction_e
    in 135..179 -> R.string.direction_se
    in 180..224 -> R.string.direction_s
    in 225..269 -> R.string.direction_sw
    in 270..314 -> R.string.direction_w
    else -> R.string.direction_n
}

fun Float.millimetresToInches() = this * 0.03937f
fun Float.metersPerSecondToKilometresPerHour() = this * 3.6f
fun Float.metersPerSecondToMilesPerHour() = this * 2.2369f