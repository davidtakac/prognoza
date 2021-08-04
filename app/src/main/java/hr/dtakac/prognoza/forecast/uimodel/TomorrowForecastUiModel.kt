package hr.dtakac.prognoza.forecast.uimodel

sealed class TomorrowForecastUiModel {
    data class Success(
        val summary: DayUiModel,
        val hours: List<HourUiModel>
    ): TomorrowForecastUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TomorrowForecastUiModel()
}