package hr.dtakac.prognoza.forecast.uimodel

sealed class TodayUiModel {
    data class Success(
        val currentHour: HourUiModel,
        val otherHours: List<HourUiModel>
    ): TodayUiModel()

    data class Error(
        val errorMessageResourceId: Int
    ): TodayUiModel()
}