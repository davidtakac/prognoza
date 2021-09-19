package hr.dtakac.prognoza.utils.timeprovider

import hr.dtakac.prognoza.HOURS_AFTER_MIDNIGHT
import hr.dtakac.prognoza.utils.atStartOfDay
import java.time.ZonedDateTime

class DefaultForecastTimeProvider : ForecastTimeProvider {
    override val todayStart: ZonedDateTime
        get() = ZonedDateTime
            .now()
            .minusHours(1L)

    override val todayEnd: ZonedDateTime
        get() = todayStart
            .plusDays(1L)
            .atStartOfDay()
            .plusHours(HOURS_AFTER_MIDNIGHT)

    override val tomorrowStart: ZonedDateTime
        get() = todayEnd
            .plusHours(1L)

    override val tomorrowEnd: ZonedDateTime
        get() = tomorrowStart
            .plusHours(24L)

    override val comingStart: ZonedDateTime
        get() = tomorrowStart

    override val comingEnd: ZonedDateTime
        get() = comingStart
            .plusDays(7L)
}