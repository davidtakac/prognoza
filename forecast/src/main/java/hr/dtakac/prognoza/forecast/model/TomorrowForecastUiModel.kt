package hr.dtakac.prognoza.forecast.model

import androidx.compose.runtime.Immutable

sealed class TomorrowForecastUiModel {
    @Immutable
    data class Success(
        val summary: DayUiModel,
        val hours: List<HourUiModel>
    ) : TomorrowForecastUiModel()

    @Immutable
    object None : TomorrowForecastUiModel()
}