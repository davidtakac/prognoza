package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.maxTemperature
import hr.dtakac.prognoza.database.entity.minTemperature
import hr.dtakac.prognoza.database.entity.representativeWeatherIcon
import hr.dtakac.prognoza.database.entity.toHourUiModels
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TomorrowViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineScopeViewModel(coroutineScope) {
    private val _tomorrowForecast = MutableLiveData<TomorrowUiModel>()
    val tomorrowForecast: LiveData<TomorrowUiModel> get() = _tomorrowForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTomorrowForecast() {
        _isLoading.value = true
        coroutineScope.launch {
            val tomorrowHours = forecastRepository.getTomorrowForecastHours()
            val weatherIconAsync = async(dispatcherProvider.default) { tomorrowHours.representativeWeatherIcon() }
            val lowTempAsync = async(dispatcherProvider.default) { tomorrowHours.minTemperature() }
            val highTempAsync = async(dispatcherProvider.default) { tomorrowHours.maxTemperature() }
            val uiModelsAsync = async(dispatcherProvider.default) { tomorrowHours.toHourUiModels() }
            val forecastTomorrowUiModel = TomorrowUiModel(
                dateTime = ZonedDateTime.now().atStartOfDay().plusDays(1),
                lowTemperature = lowTempAsync.await(),
                highTemperature = highTempAsync.await(),
                weatherIcon = weatherIconAsync.await(),
                hours = uiModelsAsync.await()
            )
            _tomorrowForecast.value = forecastTomorrowUiModel
            _isLoading.value = false
        }
    }
}