package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

sealed class ComingForecastUiModel {
    @Immutable
    data class Success(
        val instants: List<InstantUiModel>
    ) : ComingForecastUiModel()

    @Immutable
    object None : ComingForecastUiModel()
}