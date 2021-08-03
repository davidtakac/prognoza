package hr.dtakac.prognoza.forecast.uimodel

sealed class DaysUiModel {
    data class Success(
        val days: List<DayUiModel>
    ) : DaysUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ) : DaysUiModel()
}