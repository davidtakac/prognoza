package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

interface ForecastUiModel

@Immutable
data class ComingForecastUiModel(
    val days: List<DayUiModel>
) : ForecastUiModel

@Immutable
data class TodayForecastUiModel(
    val currentHour: HourUiModel,
    val otherHours: List<HourUiModel>
) : ForecastUiModel

@Immutable
data class TomorrowForecastUiModel(
    val summary: DayUiModel,
    val hours: List<HourUiModel>
) : ForecastUiModel