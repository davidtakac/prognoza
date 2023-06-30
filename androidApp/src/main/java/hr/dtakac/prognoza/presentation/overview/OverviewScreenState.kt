package hr.dtakac.prognoza.presentation.overview

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.presentation.TextResource

data class OverviewScreenState(
    val loading: Boolean = false,
    val placeName: TextResource? = null,
    val error: TextResource? = null,
    val data: OverviewDataState? = null
)

data class OverviewDataState(
    val now: OverviewNowState,
    val hours: List<OverviewHourState>
)

data class OverviewNowState(
    val time: TextResource,
    val temperature: TextResource,
    val maximumTemperature: TextResource,
    val minimumTemperature: TextResource,
    val feelsLikeTemperature: TextResource,
    @DrawableRes val weatherIcon: Int,
    val weatherDescription: TextResource,
)

sealed interface OverviewHourState {
    data class Weather(
        val time: TextResource,
        val temperature: TextResource,
        @DrawableRes val weatherIcon: Int,
        val pop: TextResource?,
    ) : OverviewHourState

    data class Sunrise(val time: TextResource): OverviewHourState

    data class Sunset(val time: TextResource): OverviewHourState
}