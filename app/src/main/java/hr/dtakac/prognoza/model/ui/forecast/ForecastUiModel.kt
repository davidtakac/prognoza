package hr.dtakac.prognoza.model.ui.forecast

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.model.ui.cell.DayUiModel
import hr.dtakac.prognoza.model.ui.cell.HourUiModel

interface ForecastUiModel

@Immutable
data class DaysForecastUiModel(
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