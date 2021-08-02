package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.atStartOfDay
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.*
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
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
                getNewForecast()
            }
        }
    }

    private suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        val result = forecastRepository.getTomorrowForecastHours(selectedPlaceId)
        when (result) {
            is ForecastResult.Success -> handleSuccess(result)
            is ForecastResult.Error -> handleError(result)
        }

    }

    private suspend fun handleSuccess(result: ForecastResult.Success) {
        val weatherIconAsync =
            coroutineScope.async(dispatcherProvider.default) { result.hours.representativeWeatherIcon() }
        val lowTempAsync =
            coroutineScope.async(dispatcherProvider.default) { result.hours.minTemperature() }
        val highTempAsync =
            coroutineScope.async(dispatcherProvider.default) { result.hours.maxTemperature() }
        val uiModelsAsync =
            coroutineScope.async(dispatcherProvider.default) { result.hours.toHourUiModels() }
        val forecastTomorrowUiModel = TomorrowUiModel(
            dateTime = ZonedDateTime.now().atStartOfDay().plusDays(1),
            lowTemperature = lowTempAsync.await(),
            highTemperature = highTempAsync.await(),
            weatherIcon = weatherIconAsync.await(),
            hours = uiModelsAsync.await()
        )
        currentMeta = result.meta
        _tomorrowForecast.value = forecastTomorrowUiModel
        _isLoading.value = false
    }

    private fun handleError(error: ForecastResult.Error) {
        // todo: same as in today view model
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _tomorrowForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}