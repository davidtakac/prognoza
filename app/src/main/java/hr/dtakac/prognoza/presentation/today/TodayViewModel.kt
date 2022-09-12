package hr.dtakac.prognoza.presentation.today

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.TodayForecastResult
import hr.dtakac.prognoza.domain.usecase.gettodayforecast.GetTodayForecast
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayForecast: GetTodayForecast
) : ViewModel() {

    private val _state: MutableState<TodayUiState> = mutableStateOf(TodayUiState())
    val state: State<TodayUiState> get() = _state

    init {
        getState()
        scheduleStateRefresh()
    }

    private fun getState() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = when (val result = getTodayForecast()) {
                is TodayForecastResult.Success -> _state.value.copy(
                    content = mapToTodayContent(
                        result.placeName,
                        result.todayForecast,
                        result.temperatureUnit,
                        result.windUnit,
                        result.precipitationUnit
                    ),
                    isLoading = false
                )
                is TodayForecastResult.Error -> _state.value.copy(
                    error = mapToTodayError(result),
                    isLoading = false
                )
            }
        }
    }

    private fun scheduleStateRefresh() {
        viewModelScope.launch {
            while (isActive) {
                delay(Duration.ofMinutes(Random.nextLong(45L, 75L)).toMillis())
                getState()
            }
        }
    }
}