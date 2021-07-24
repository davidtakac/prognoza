package hr.dtakac.prognoza

import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())

fun <T> List<T>.mostCommon(): T? =
    groupingBy { it }.eachCount().maxByOrNull { it.value }?.key