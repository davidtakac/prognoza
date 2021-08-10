package hr.dtakac.prognoza.forecast.uimodel

data class TodayForecast(
    val currentHour: HourUiModel,
    val precipitationForecast: Float?,
    val otherHours: List<HourUiModel>
)