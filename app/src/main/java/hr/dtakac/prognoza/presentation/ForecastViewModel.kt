package hr.dtakac.prognoza.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.getforecast.GetForecastResult
import hr.dtakac.prognoza.domain.usecase.getforecast.GetForecast
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecast: GetForecast
) : ViewModel() {

    private val _state: MutableState<ForecastUiState> = mutableStateOf(ForecastUiState())
    val state: State<ForecastUiState> get() = _state

    fun getState() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = when (val result = getForecast()) {
                is GetForecastResult.Success -> _state.value.copy(
                    today = mapToTodayContent(
                        result.placeName,
                        result.forecast.current,
                        result.forecast.today,
                        result.temperatureUnit,
                        result.windUnit,
                        result.precipitationUnit
                    ),
                    comingContent = ComingContent(listOf()),
                    isLoading = false
                )
                is GetForecastResult.Error -> _state.value.copy(
                    error = mapToTodayError(result),
                    isLoading = false
                )
            }
        }
        //testColorChanges()
    }

    private fun testColorChanges() {
        viewModelScope.launch {
            while (isActive) {
                delay(3000L)
                _state.value = _state.value.copy(
                    today = _state.value.today?.copy(
                        shortDescription = ForecastDescription.Short.values().random()
                    )
                )
            }
        }
    }
}