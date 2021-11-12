package hr.dtakac.prognoza.forecast.uistate

import androidx.compose.runtime.Immutable

@Immutable
data class OutdatedForecastUiState(
    val reason: Int?
)