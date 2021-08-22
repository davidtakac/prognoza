package hr.dtakac.prognoza.uimodel.forecast

import hr.dtakac.prognoza.uimodel.cell.DayCellModel
import hr.dtakac.prognoza.uimodel.cell.HourCellModel

interface ForecastUiModel

data class DaysForecastUiModel(
    val days: List<DayCellModel>
): ForecastUiModel

data class TodayForecastUiModel(
    val currentHour: HourCellModel,
    val precipitationForecast: Float?,
    val otherHours: List<HourCellModel>
): ForecastUiModel

data class TomorrowForecastUiModel(
    val summary: DayCellModel,
    val hours: List<HourCellModel>
): ForecastUiModel