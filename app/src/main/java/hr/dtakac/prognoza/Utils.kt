package hr.dtakac.prognoza

import java.time.ZonedDateTime

fun getTomorrow(): ZonedDateTime {
    val now = ZonedDateTime.now()
    return now.minusHours(now.hour.toLong()).plusDays(1)
}

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    minusHours(hour.toLong())
        .minusMinutes(minute.toLong())
        .minusSeconds(second.toLong())
        .minusNanos(nano.toLong())

fun <T> List<T>.mostCommon(): T {
    return groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }!!
        .key
}