package hr.dtakac.prognoza.forecast.uimodel

sealed class TodayForecastUiModel {
    data class Success(
        val currentHour: HourUiModel,
        val otherHours: List<HourUiModel>
    ): TodayForecastUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TodayForecastUiModel()
}