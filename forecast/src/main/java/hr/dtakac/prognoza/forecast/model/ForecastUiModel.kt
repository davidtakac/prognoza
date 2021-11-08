package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

interface ForecastUiModel

@Immutable
data class DaysForecastUiModel(
    val days: List<hr.dtakac.prognoza.forecast.model.DayUiModel>
) : ForecastUiModel

@Immutable
data class TodayForecastUiModel(
    val currentHour: hr.dtakac.prognoza.forecast.model.HourUiModel,
    val otherHours: List<hr.dtakac.prognoza.forecast.model.HourUiModel>
) : ForecastUiModel

@Immutable
data class TomorrowForecastUiModel(
    val summary: hr.dtakac.prognoza.forecast.model.DayUiModel,
    val hours: List<hr.dtakac.prognoza.forecast.model.HourUiModel>
) : ForecastUiModel