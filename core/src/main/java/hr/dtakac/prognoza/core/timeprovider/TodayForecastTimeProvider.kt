package hr.dtakac.prognoza.core.timeprovider

import hr.dtakac.prognoza.core.utils.HOURS_AFTER_MIDNIGHT
import java.time.ZonedDateTime

class TodayForecastTimeProvider : ForecastTimeProvider {
    override val start: ZonedDateTime
        get() = ZonedDateTime
            .now()
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

    override val end: ZonedDateTime
        get() {
            val hoursLeftInToday = 24 - start.hour
            return start.plusHours(
                hoursLeftInToday + HOURS_AFTER_MIDNIGHT
            )
        }
}