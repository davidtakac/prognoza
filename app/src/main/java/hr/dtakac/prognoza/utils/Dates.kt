package hr.dtakac.prognoza.utils

import java.time.ZoneId
import java.time.ZonedDateTime

const val HOURS_AFTER_MIDNIGHT = 6L

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())

val forecastStartOfToday: ZonedDateTime
    get() = ZonedDateTime.now().minusHours(1L)

val forecastEndOfToday: ZonedDateTime
    get() = forecastStartOfToday.plusDays(1L).atStartOfDay().plusHours(HOURS_AFTER_MIDNIGHT)

val forecastStartOfTomorrow: ZonedDateTime
    get() = ZonedDateTime
        .now()
        .atStartOfDay()
        .plusDays(1L)
        .plusHours(HOURS_AFTER_MIDNIGHT + 1L)

val forecastEndOfTomorrow: ZonedDateTime
    get() = forecastStartOfTomorrow.plusDays(1L).minusHours(1L)

val forecastStartOfComing: ZonedDateTime
    get() = forecastStartOfTomorrow

val forecastEndOfComing: ZonedDateTime
    get() = forecastStartOfComing.plusDays(7L).minusHours(1L)