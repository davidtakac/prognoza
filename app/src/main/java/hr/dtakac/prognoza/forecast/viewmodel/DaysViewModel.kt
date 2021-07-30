package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.database.entity.toDayUiModels
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DaysViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
) : CoroutineScopeViewModel(coroutineScope) {
    private val _daysForecast = MutableLiveData<OtherDaysUiModel>()
    val daysForecast: LiveData<OtherDaysUiModel> get() = _daysForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getOtherDaysForecast() {
        _isLoading.value = true
        coroutineScope.launch {
            val uiModels = forecastRepository.getOtherDaysForecastHours().toDayUiModels(this)
            _daysForecast.value = OtherDaysUiModel(days = uiModels)
            _isLoading.value = false
        }
    }
}