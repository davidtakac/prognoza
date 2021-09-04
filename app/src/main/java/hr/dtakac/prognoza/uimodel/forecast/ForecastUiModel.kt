package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import hr.dtakac.prognoza.uimodel.cell.HourUiModel

interface ForecastUiModel

data class DaysForecastUiModel(
    val days: List<DayUiModel>
) : ForecastUiModel

data class TodayForecastUiModel(
    val currentHour: HourUiModel,
    val otherHours: List<HourUiModel>
) : ForecastUiModel

data class TomorrowForecastUiModel(
    val summary: DayUiModel,
    val hours: List<HourUiModel>
) : ForecastUiModel