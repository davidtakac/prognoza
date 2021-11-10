package hr.dtakac.prognoza.core.timeprovider

import java.time.ZonedDateTime

class TomorrowForecastTimeProvider(
    private val todayForecastTimeProvider: TodayForecastTimeProvider
) : ForecastTimeProvider {
    override val start: ZonedDateTime
        get() = todayForecastTimeProvider.end.plusHours(1L)

    override val end: ZonedDateTime
        get() = start.plusHours(23L)
}