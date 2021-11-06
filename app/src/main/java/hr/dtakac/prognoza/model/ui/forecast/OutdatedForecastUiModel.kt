package hr.dtakac.prognoza.model.ui.forecast

import androidx.compose.runtime.Immutable

@Immutable
data class OutdatedForecastUiModel(
    val reason: Int?
)