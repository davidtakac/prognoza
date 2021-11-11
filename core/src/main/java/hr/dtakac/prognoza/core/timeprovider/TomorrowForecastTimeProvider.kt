package hr.dtakac.prognoza.core.timeprovider

import java.time.ZonedDateTime

class TomorrowForecastTimeProvider(
    private val referenceTimeProvider: ForecastTimeProvider
) : ForecastTimeProvider {
    override val start: ZonedDateTime
        get() = referenceTimeProvider.end.plusHours(1L)

    override val end: ZonedDateTime
        get() = start.plusHours(23L)
}