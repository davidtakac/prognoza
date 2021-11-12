package hr.dtakac.prognoza.forecast.uistate

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.forecast.model.InstantUiModel
import hr.dtakac.prognoza.forecast.model.TodaySummaryUiModel

sealed class TodayForecastUiState {
    @Immutable
    data class Success(
        val todaySummary: TodaySummaryUiModel,
        val instants: List<InstantUiModel>
    ) : TodayForecastUiState()

    @Immutable
    object None : TodayForecastUiState()
}