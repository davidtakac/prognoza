package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

sealed class TodayForecastUiModel {
    @Immutable
    data class Success(
        val instants: List<InstantUiModel>
    ) : TodayForecastUiModel()

    @Immutable
    object None : TodayForecastUiModel()
}
