package hr.dtakac.prognoza.forecast.uistate

import androidx.compose.runtime.Immutable
import hr.dtakac.prognoza.forecast.model.InstantUiModel

sealed class ComingForecastUiState {
    @Immutable
    data class Success(
        val instants: List<InstantUiModel>
    ) : ComingForecastUiState()

    @Immutable
    object None : ComingForecastUiState()
}