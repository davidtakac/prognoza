package hr.dtakac.prognoza.ui.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.common.TextResource
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherDescription
import hr.dtakac.prognoza.ui.common.wmoCodeToWeatherIcon
import hr.dtakac.prognoza.shared.entity.OverviewHour
import hr.dtakac.prognoza.shared.usecase.GetOverview
import hr.dtakac.prognoza.shared.usecase.GetSelectedPlace
import kotlinx.coroutines.launch
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
                            overview.now.wmoCode,
                            day = overview.now.day
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
                                weatherIcon = wmoCodeToWeatherIcon(hour.wmoCode, day = hour.day),
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
                            // TODO: figure out a way to provide day/night normally. You can probably
                            //  do maxOf { wmoCode}. Most likely they're ordered by severity already.
                            //  Then just get the max's day variable and that's it.
                            weatherIcon = wmoCodeToWeatherIcon(day.wmoCode, day = true),
                            minimumTemperature = TextResource.fromTemperature(day.minimumTemperature),
                            maximumTemperature = TextResource.fromTemperature(day.maximumTemperature),
                            temperatureBarStartFraction = 1f - (overview.days.minimumTemperature.value / day.minimumTemperature.value).toFloat(),
                            temperatureBarEndFraction = 1f - (day.maximumTemperature.value / overview.days.maximumTemperature.value).toFloat(),
                        )
                    },
                ),
                error = null,
                loading = false
            )
        }
    }
}