package hr.dtakac.prognoza.utils.timeprovider

import java.time.ZonedDateTime

interface ForecastTimeProvider {
    val todayStart: ZonedDateTime
    val todayEnd: ZonedDateTime
    val tomorrowStart: ZonedDateTime
    val tomorrowEnd: ZonedDateTime
    val comingStart: ZonedDateTime
    val comingEnd: ZonedDateTime
}