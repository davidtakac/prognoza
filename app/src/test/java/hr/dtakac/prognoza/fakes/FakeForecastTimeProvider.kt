package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.HOURS_AFTER_MIDNIGHT
import hr.dtakac.prognoza.common.utils.atStartOfDay
import hr.dtakac.prognoza.common.timeprovider.ForecastTimeProvider
import java.time.ZonedDateTime

class FakeForecastTimeProvider : ForecastTimeProvider {
    override val todayStart: ZonedDateTime
        get() = FakeForecastRepository.now

    override val todayEnd: ZonedDateTime
        get() = todayStart
            .plusDays(1L)
            .atStartOfDay()
            .plusHours(HOURS_AFTER_MIDNIGHT)

    override val tomorrowStart: ZonedDateTime
        get() = todayEnd.plusHours(1L)

    override val tomorrowEnd: ZonedDateTime
        get() = tomorrowStart.plusHours(24L)

    override val comingStart: ZonedDateTime
        get() = tomorrowStart

    override val comingEnd: ZonedDateTime
        get() = tomorrowStart.plusDays(7L)
}