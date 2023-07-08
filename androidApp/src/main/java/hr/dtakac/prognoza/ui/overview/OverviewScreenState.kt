package hr.dtakac.prognoza.ui.overview

import androidx.annotation.DrawableRes
import hr.dtakac.prognoza.ui.common.TextResource

data class OverviewScreenState(
    val loading: Boolean = false,
    val placeName: TextResource? = null,
    val error: TextResource? = null,
    val data: OverviewDataState? = null
)

data class OverviewDataState(
    val now: OverviewNowState,
    val hours: List<OverviewHourState>,
    val days: List<OverviewDayState>
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

data class OverviewDayState(
    val dayOfWeek: TextResource,
    val pop: TextResource?,
    @DrawableRes val weatherIcon: Int,
    val minimumTemperature: TextResource,
    val maximumTemperature: TextResource,
    val temperatureBarStartFraction: Float,
    val temperatureBarEndFraction: Float
)