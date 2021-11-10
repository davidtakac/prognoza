package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

sealed class TodayForecastUiModel {
    @Immutable
    data class Success(
        val currentHour: HourUiModel,
        val otherHours: List<HourUiModel>
    ) : TodayForecastUiModel()

    @Immutable
    object None : TodayForecastUiModel()
}
