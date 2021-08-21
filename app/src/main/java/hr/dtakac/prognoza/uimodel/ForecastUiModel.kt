package hr.dtakac.prognoza.uimodel

interface ForecastUiModel

data class DaysForecastUiModel(
    val days: List<DayUiModel>
): ForecastUiModel

data class TodayForecastUiModel(
    val currentHour: HourUiModel,
    val precipitationForecast: Float?,
    val otherHours: List<HourUiModel>
): ForecastUiModel

data class TomorrowForecastUiModel(
    val summary: DayUiModel,
    val hours: List<HourUiModel>
): ForecastUiModel