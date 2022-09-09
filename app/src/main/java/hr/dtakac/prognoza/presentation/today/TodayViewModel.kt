package hr.dtakac.prognoza.presentation.today

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecastResult
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.GetTodayForecast
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayForecast: GetTodayForecast
) : ViewModel() {

    private val _state: MutableState<TodayUiState> = mutableStateOf(TodayUiState.Loading)
    val state: State<TodayUiState> get() = _state

    fun getState() {
        viewModelScope.launch {
            _state.value = TodayUiState.Loading
            _state.value = when (val result = getTodayForecast()) {
                is TodayForecastResult.Success -> mapToTodayUiState(
                    result.placeName,
                    result.todayForecast,
                    result.temperatureUnit,
                    result.windUnit,
                    result.precipitationUnit
                )
                is TodayForecastResult.Error -> mapToEmptyTodayUiState(result)
            }
        }
    }
}