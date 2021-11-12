package hr.dtakac.prognoza.forecast.uistate

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.forecast.model.InstantUiModel

sealed class TomorrowForecastUiState {
    @Immutable
    data class Success(
        val instants: List<InstantUiModel>
    ) : TomorrowForecastUiState()

    @Immutable
    object None : TomorrowForecastUiState()
}