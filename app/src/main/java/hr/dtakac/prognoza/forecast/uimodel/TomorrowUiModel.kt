package hr.dtakac.prognoza.forecast.uimodel

sealed class TomorrowUiModel {
    data class Success(
        val summary: DayUiModel,
        val hours: List<HourUiModel>
    ): TomorrowUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TomorrowUiModel()
}