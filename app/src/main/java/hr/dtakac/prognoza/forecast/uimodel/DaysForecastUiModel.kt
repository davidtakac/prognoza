package hr.dtakac.prognoza.forecast.uimodel

sealed class DaysForecastUiModel {
    data class Success(
        val days: List<DayUiModel>
    ) : DaysForecastUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ) : DaysForecastUiModel()
}