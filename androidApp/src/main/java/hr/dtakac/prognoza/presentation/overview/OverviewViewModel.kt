package hr.dtakac.prognoza.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
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
                    temperature = TextResource.fromTemperature(overview.temperature),
                    maximumTemperature = TextResource.fromTemperature(overview.maximumTemperature),
                    minimumTemperature = TextResource.fromTemperature(overview.minimumTemperature),
                    feelsLikeTemperature = TextResource.fromTemperature(overview.feelsLike),
                    // TODO: replace with actual mappings based on WMO code
                    weatherIcon = R.drawable.dark_01d,
                    weatherDescription = TextResource.fromString("Cloudy"),
                    backgroundImage = 0
                ),
                error = null,
                loading = false
            )
        }
    }
}