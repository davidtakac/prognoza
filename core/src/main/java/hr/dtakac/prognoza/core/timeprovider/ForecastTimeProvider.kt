package hr.dtakac.prognoza.core.timeprovider

import java.time.ZonedDateTime

interface ForecastTimeProvider {
    val start: ZonedDateTime
    val end: ZonedDateTime
}