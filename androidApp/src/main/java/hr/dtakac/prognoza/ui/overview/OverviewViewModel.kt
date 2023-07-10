package hr.dtakac.prognoza.ui.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.shared.entity.OverviewHour
import hr.dtakac.prognoza.shared.usecase.GetOverview
import hr.dtakac.prognoza.shared.usecase.GetSelectedPlace
import hr.dtakac.prognoza.ui.common.TextResource
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherDescription
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherIcon
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
  private val getSelectedPlaceUseCase: GetSelectedPlace,
  private val getOverviewUseCase: GetOverview
) : ViewModel() {
  var state by mutableStateOf(OverviewScreenState())
    private set

  fun getOverview() {
    viewModelScope.launch {
      state = state.copy(loading = true)

      val selectedPlace = getSelectedPlaceUseCase()
      if (selectedPlace == null) {
        state = state.copy(
          error = TextResource.fromResId(R.string.forecast_msg_empty_no_place),
          loading = false
        )
        return@launch
      }
      state = state.copy(placeName = TextResource.fromString(selectedPlace.name))

      val overview = getOverviewUseCase(selectedPlace.coordinates)
      if (overview == null) {
        state = state.copy(
          error = TextResource.fromResId(R.string.forecast_err),
          loading = false
        )
        return@launch
      }

      state = state.copy(
        data = OverviewDataState(
          now = OverviewNowState(
            time = TextResource.fromResId(R.string.forecast_label_now),
            temperature = TextResource.fromTemperature(overview.now.temperature),
            maximumTemperature = TextResource.fromTemperature(overview.now.maximumTemperature),
            minimumTemperature = TextResource.fromTemperature(overview.now.minimumTemperature),
            feelsLikeTemperature = TextResource.fromTemperature(overview.now.feelsLike),
            weatherIcon = wmoCodeToWeatherIcon(
              wmoCode = overview.now.wmoCode,
              isDay = overview.now.isDay
            ),
            weatherDescription = TextResource.fromResId(
              wmoCodeToWeatherDescription(
                overview.now.wmoCode
              )
            ),
          ),
          hours = overview.hours.mapIndexed { idx, hour ->
            when (hour) {
              is OverviewHour.Sunrise -> OverviewHourState.Sunrise(
                time = TextResource.fromUnixSecondToShortTime(
                  hour.unixSecond,
                  timeZone = overview.timeZone
                )
              )
              is OverviewHour.Sunset -> OverviewHourState.Sunset(
                time = TextResource.fromUnixSecondToShortTime(
                  hour.unixSecond,
                  timeZone = overview.timeZone
                )
              )
              is OverviewHour.Weather -> OverviewHourState.Weather(
                time = if (idx == 0) TextResource.fromResId(R.string.forecast_label_now)
                else TextResource.fromUnixSecondToShortTime(
                  unixSecond = hour.unixSecond,
                  timeZone = overview.timeZone
                ),
                temperature = TextResource.fromTemperature(hour.temperature),
                weatherIcon = wmoCodeToWeatherIcon(
                  hour.wmoCode,
                  isDay = hour.isDay
                ),
                pop = hour.pop.takeUnless { it == 0 }
                  ?.let(TextResource::fromPercentage)
              )
            }
          },
          days = overview.days.days.mapIndexed { idx, day ->
            OverviewDayState(
              dayOfWeek = if (idx == 0) TextResource.fromResId(R.string.forecast_label_today)
              else TextResource.fromUnixSecondToShortDayOfWeek(
                unixSecond = day.unixSecond,
                timeZone = overview.timeZone
              ),
              pop = day.maximumPop.takeUnless { it == 0 }?.let(TextResource::fromPercentage),
              weatherIcon = wmoCodeToWeatherIcon(
                wmoCode = day.representativeWmoCode,
                isDay = day.representativeWmoCodeIsDay
              ),
              minimumTemperature = TextResource.fromTemperature(day.minimumTemperature),
              maximumTemperature = TextResource.fromTemperature(day.maximumTemperature),
              temperatureBarStartFraction = ((day.minimumTemperature.value - overview.days.minimumTemperature.value) / (overview.days.maximumTemperature.value - overview.days.minimumTemperature.value)).toFloat(),
              temperatureBarEndFraction = ((overview.days.maximumTemperature.value - day.maximumTemperature.value) / (overview.days.maximumTemperature.value - overview.days.minimumTemperature.value)).toFloat(),
              currentTemperatureCenterFraction = if (idx == 0) ((overview.now.temperature.value - overview.days.minimumTemperature.value) / (overview.days.maximumTemperature.value - overview.days.minimumTemperature.value)).toFloat() else null
            )
          },
          details = buildList {
            val rainfall = OverviewDetailState.Precipitation(
              amountInLastPeriod = TextResource.fromLength(overview.rainfall.amountInLastPeriod),
              hoursInLastPeriod = TextResource.fromResId(
                id = R.string.precipitation_value_last_hours,
                overview.rainfall.hoursInLastPeriod
              ),
              nextExpected = overview.rainfall.startUnixSecondOfNextExpectedAmount?.let {
                val nowDate = Clock.System.now().toLocalDateTime(overview.timeZone).date
                val nextExpectedDate = Instant.fromEpochSeconds(it).toLocalDateTime(overview.timeZone).date
                val nextExpectedAmount = TextResource.fromLength(overview.rainfall.nextExpectedAmount)
                if (nextExpectedDate == nowDate) {
                  TextResource.fromResId(
                    id = R.string.precipitation_value_next_expected_from_time,
                    nextExpectedAmount,
                    TextResource.fromUnixSecondToShortTime(it, overview.timeZone)
                  )
                } else {
                  TextResource.fromResId(
                    id = R.string.precipitation_value_next_expected_on_day,
                    nextExpectedAmount,
                    TextResource.fromUnixSecondToShortDayOfWeek(it, overview.timeZone)
                  )
                }
              } ?: TextResource.fromResId(R.string.precipitation_value_none_expected, overview.days.days.size),
              isSnow = false
            )

            add(rainfall)
          }
        ),
        error = null,
        loading = false
      )
    }
  }
}