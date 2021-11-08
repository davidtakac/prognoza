package hr.dtakac.prognoza.core.timeprovider

import hr.dtakac.prognoza.core.utils.HOURS_AFTER_MIDNIGHT
import java.time.ZonedDateTime

class DefaultForecastTimeProvider : ForecastTimeProvider {
    override val todayStart: ZonedDateTime
        get() = ZonedDateTime
            .now()
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

    override val todayEnd: ZonedDateTime
        get() {
            val hoursLeftInToday = 24 - todayStart.hour
            return todayStart.plusHours(
                hoursLeftInToday + HOURS_AFTER_MIDNIGHT
            )
        }

    override val tomorrowStart: ZonedDateTime
        get() = todayEnd
            .plusHours(1L)

    override val tomorrowEnd: ZonedDateTime
        get() = tomorrowStart
            .plusHours(23L)

    override val comingStart: ZonedDateTime
        get() = tomorrowStart

    override val comingEnd: ZonedDateTime
        get() = comingStart
            .plusDays(7L)
}