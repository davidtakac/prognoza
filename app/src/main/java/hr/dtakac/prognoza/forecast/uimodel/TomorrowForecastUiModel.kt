package hr.dtakac.prognoza.forecast.uimodel

data class TomorrowForecast(
    val summary: DayUiModel,
    val hours: List<HourUiModel>
)