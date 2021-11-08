package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

@Immutable
data class OutdatedForecastUiModel(
    val reason: Int?
)