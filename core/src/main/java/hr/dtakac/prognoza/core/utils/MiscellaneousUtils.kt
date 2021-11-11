package hr.dtakac.prognoza.core.utils

import hr.dtakac.prognoza.core.model.database.ForecastMeta
import java.time.Duration
import java.time.ZonedDateTime

fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

fun ForecastMeta?.hasExpired(): Boolean = try {
    ZonedDateTime.now() > this?.expires!!
} catch (e: Exception) {
    true
}

fun ZonedDateTime.isSameHourAsNow(): Boolean {
    return Duration.between(this, ZonedDateTime.now()).toMinutes() in (0L..60L)
}