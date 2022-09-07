package hr.dtakac.prognoza.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecases.GetTodayForecast
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayForecastViewModel @Inject constructor(
    private val getTodayForecast: GetTodayForecast
) : ViewModel() {
    fun getState() {
        viewModelScope.launch {
            val result = getTodayForecast()
            val t = 0
        }
    }
}