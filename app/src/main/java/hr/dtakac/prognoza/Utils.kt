package hr.dtakac.prognoza

import java.time.ZonedDateTime

fun getTomorrow(): ZonedDateTime {
    val now = ZonedDateTime.now()
    return now.minusHours(now.hour.toLong()).plusDays(1)
}