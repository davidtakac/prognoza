package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.toHourUiModels
import hr.dtakac.prognoza.forecast.uimodel.TodayUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class TodayViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineScopeViewModel(coroutineScope) {
    private val _todayForecast = MutableLiveData<TodayUiModel>()
    val todayForecast: LiveData<TodayUiModel> get() = _todayForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTodayForecast() {
        _isLoading.value = true
        coroutineScope.launch {
            val hours = forecastRepository.getTodayForecastHours()
            val uiModels = withContext(dispatcherProvider.default) { hours.toHourUiModels() }
            val forecastTodayUiModel = TodayUiModel(
                dateTime = ZonedDateTime.now(),
                currentTemperature = uiModels[0].temperature,
                weatherIcon = uiModels[0].weatherIcon,
                nextHours = uiModels
            )
            _todayForecast.value = forecastTodayUiModel
            _isLoading.value = false
        }
    }
}