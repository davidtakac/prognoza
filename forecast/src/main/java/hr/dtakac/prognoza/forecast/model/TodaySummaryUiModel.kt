package hr.dtakac.prognoza.forecast.model

import java.time.ZonedDateTime

data class TodaySummaryUiModel(
    val time: ZonedDateTime,
    val morning: PeriodOfDayUiModel? = null,
    val afternoon: PeriodOfDayUiModel? = null,
    val evening: PeriodOfDayUiModel? = null,
    val night: PeriodOfDayUiModel? = null
)