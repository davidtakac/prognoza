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
                        maximumTemperature = TextResource.fromTemperature(overview.days.maximumTemperature),
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
                    hours = overview.hours.mapIndexed { idx, h ->
                        when (h) {
                            is OverviewHour.Sunrise -> OverviewHourState.Sunrise(
                                time = TextResource.fromTime(
                                    h.unixSecond,
                                    timeZone = overview.timeZone
                                )
                            )
                            is OverviewHour.Sunset -> OverviewHourState.Sunset(
                                time = TextResource.fromTime(
                                    h.unixSecond,
                                    timeZone = overview.timeZone
                                )
                            )
                            is OverviewHour.Weather -> OverviewHourState.Weather(
                                time = if (idx == 0) TextResource.fromResId(R.string.forecast_label_now)
                                else TextResource.fromTime(
                                    h.unixSecond,
                                    timeZone = overview.timeZone
                                ),
                                temperature = TextResource.fromTemperature(h.temperature),
                                weatherIcon = wmoCodeToWeatherIcon(h.wmoCode, day = h.day),
                                pop = h.pop.takeUnless { it == 0 }
                                    ?.let(TextResource::fromPercentage)
                            )
                        }
                    }
                ),
                error = null,
                loading = false
            )
        }
    }
}