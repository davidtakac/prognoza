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
  val days: List<OverviewDayState>,
  val details: List<OverviewDetailState>
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

  data class Sunrise(val time: TextResource) : OverviewHourState

  data class Sunset(val time: TextResource) : OverviewHourState
}

data class OverviewDayState(
  val dayOfWeek: TextResource,
  val pop: TextResource?,
  @DrawableRes val weatherIcon: Int,
  val minimumTemperature: TextResource,
  val maximumTemperature: TextResource,
  val temperatureBarStartFraction: Float,
  val temperatureBarEndFraction: Float,
  val currentTemperatureCenterFraction: Float?
)

sealed interface OverviewDetailState {
  data class Precipitation(
    val amountInLastPeriod: TextResource,
    val hoursInLastPeriod: TextResource,
    val nextExpected: TextResource,
    val isSnow: Boolean
  ) : OverviewDetailState

  data class UvIndex(
    val uvIndex: TextResource,
    val level: TextResource,
    val valueCenterFraction: Float,
    val recommendations: TextResource
  ) : OverviewDetailState

  data class FeelsLike(
    val feelsLike: TextResource,
    val description: TextResource
  ) : OverviewDetailState

  data class Wind(
    val speed: TextResource,
    val maximumGust: TextResource,
    val angle: Float
  ) : OverviewDetailState

  data class Humidity(
    val humidity: TextResource,
    val dewPoint: TextResource
  ) : OverviewDetailState
}