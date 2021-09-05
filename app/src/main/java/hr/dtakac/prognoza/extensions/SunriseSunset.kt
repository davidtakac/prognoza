package hr.dtakac.prognoza.extensions

import java.time.ZonedDateTime
import kotlin.math.sin

fun calculateZenithAngle(
    lat: Double,
    lon: Double,
    date: ZonedDateTime
): Double {
    val lstm = 0.2617994 * date.offset.totalSeconds / 3600
    return 0.0
}