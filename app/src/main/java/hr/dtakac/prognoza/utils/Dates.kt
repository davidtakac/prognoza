package hr.dtakac.prognoza.utils

import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.atStartOfDay(): ZonedDateTime =
    toLocalDate().atStartOfDay(ZoneId.systemDefault())

fun ZonedDateTime.isTomorrow(): Boolean =
    withZoneSameInstant(ZoneId.systemDefault())
        .atStartOfDay() == ZonedDateTime.now().plusDays(1L).atStartOfDay()