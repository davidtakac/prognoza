package hr.dtakac.prognoza.presentation.forecast

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.GetForecastResult
import hr.dtakac.prognoza.domain.usecase.GetForecast
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecast: GetForecast
) : ViewModel() {
    private val _state: MutableState<ForecastState> = mutableStateOf(ForecastState())
    val state: State<ForecastState> get() = _state

    fun getState() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = when (val result = getForecast()) {
                is GetForecastResult.Success -> _state.value.copy(
                    forecast = mapToForecastUi(
                        placeName = result.placeName,
                        current = result.forecast.current,
                        today = result.forecast.today,
                        coming = result.forecast.coming,
                        temperatureUnit = result.temperatureUnit,
                        windUnit = result.windUnit,
                        precipitationUnit = result.precipitationUnit
                    ),
                    isLoading = false
                )
                is GetForecastResult.Error -> _state.value.copy(
                    error = mapToError(result),
                    isLoading = false
                )
            }
        }
        //testColorChanges()
    }

    private fun testColorChanges() {
        viewModelScope.launch {
            while (isActive) {
                delay(2000L)
                _state.value = _state.value.copy(
                    forecast = _state.value.forecast?.copy(
                        today = _state.value.forecast!!.today.copy(shortDescription = ForecastDescription.Short.values().random())
                    )
                )
            }
        }
    }
}