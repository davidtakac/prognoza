package hr.dtakac.prognoza.core.timeprovider

import java.time.ZonedDateTime

class ComingForecastTimeProvider(
    private val tomorrowForecastTimeProvider: TomorrowForecastTimeProvider
) : ForecastTimeProvider {
    override val start: ZonedDateTime
        get() = tomorrowForecastTimeProvider.start

    override val end: ZonedDateTime
        get() = start.plusDays(7L)
}