package hr.dtakac.prognoza.forecast.uistate

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

sealed class EmptyForecastUiState {
    @Immutable
    data class BecauseReason(
        @StringRes val reason: Int?
    ) : EmptyForecastUiState()

    @Immutable
    object BecauseNoSelectedPlace : EmptyForecastUiState()
}