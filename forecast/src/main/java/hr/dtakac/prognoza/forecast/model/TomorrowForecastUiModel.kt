package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

sealed class TomorrowForecastUiModel {
    @Immutable
    data class Success(
        val instants: List<InstantUiModel>
    ) : TomorrowForecastUiModel()

    @Immutable
    object None : TomorrowForecastUiModel()
}