package hr.dtakac.prognoza.core.timeprovider

import java.time.ZonedDateTime

class ComingForecastTimeProvider(
    private val referenceTimeProvider: ForecastTimeProvider
) : ForecastTimeProvider {
    override val start: ZonedDateTime
        get() = referenceTimeProvider.start

    override val end: ZonedDateTime
        get() = start.plusDays(7L)
}