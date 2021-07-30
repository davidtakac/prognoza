package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.*
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TomorrowViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    private val _tomorrowForecast = MutableLiveData<TomorrowUiModel>()
    val tomorrowForecast: LiveData<TomorrowUiModel> get() = _tomorrowForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTomorrowForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                _isLoading.value = true
                val forecastHours = forecastRepository.getTomorrowForecastHours()
                val weatherIconAsync =
                    async(dispatcherProvider.default) { forecastHours.hours.representativeWeatherIcon() }
                val lowTempAsync =
                    async(dispatcherProvider.default) { forecastHours.hours.minTemperature() }
                val highTempAsync =
                    async(dispatcherProvider.default) { forecastHours.hours.maxTemperature() }
                val uiModelsAsync =
                    async(dispatcherProvider.default) { forecastHours.hours.toHourUiModels() }
                val forecastTomorrowUiModel = TomorrowUiModel(
                    dateTime = ZonedDateTime.now().atStartOfDay().plusDays(1),
                    lowTemperature = lowTempAsync.await(),
                    highTemperature = highTempAsync.await(),
                    weatherIcon = weatherIconAsync.await(),
                    hours = uiModelsAsync.await()
                )
                currentMeta = forecastHours.meta
                _tomorrowForecast.value = forecastTomorrowUiModel
                _isLoading.value = false
            }
        }
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _tomorrowForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}