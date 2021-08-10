package hr.dtakac.prognoza.forecast.uimodel

data class TodayForecast(
    val currentHour: HourUiModel,
    val otherHours: List<HourUiModel>
)