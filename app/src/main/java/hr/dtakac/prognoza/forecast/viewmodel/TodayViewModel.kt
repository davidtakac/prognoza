package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.common.util.hasExpired
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.forecast.uimodel.TodayForecast
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TodayViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    private val _todayForecast = MutableLiveData<TodayForecast>()
    val todayForecast: LiveData<TodayForecast> get() = _todayForecast

    private val _emptyScreen = MutableLiveData<EmptyForecast?>()
    val emptyScreen: LiveData<EmptyForecast?> get() = _emptyScreen

    private val _message = MutableLiveData<Event<Int>>()
    val message: LiveData<Event<Int>> get() = _message

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTodayForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                getNewForecast()
            }
        }
    }

    private suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        when (val result = forecastRepository.getTodayForecastHours(selectedPlaceId)) {
            is ForecastResult.Success -> handleSuccess(result)
            is ForecastResult.Empty -> handleEmpty(result)
            is ForecastResult.CachedSuccess -> handleCachedSuccess(result)
            is ForecastResult.EmptyWithReason -> handleEmptyWithReason(result)
        }
        _isLoading.value = false
    }

    private suspend fun handleSuccess(result: ForecastResult.Success) {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            result.hours[0].toHourUiModel().copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            result.hours.map { it.toHourUiModel() }
        }
        val forecastTodayUiModel = TodayForecast(
            currentHour = currentHourAsync.await(),
            otherHours = otherHoursAsync.await()
        )
        currentMeta = result.meta
        _todayForecast.value = forecastTodayUiModel
        _emptyScreen.value = null
    }

    private fun handleEmpty(empty: ForecastResult.Empty) {
        _emptyScreen.value = EmptyForecast(null)
    }

    private suspend fun handleCachedSuccess(cachedResult: ForecastResult.CachedSuccess) {
        handleSuccess(cachedResult.success)
        _message.value = Event(R.string.notify_cached_result)
    }

    private fun handleEmptyWithReason(emptyWithReason: ForecastResult.EmptyWithReason) {
        _emptyScreen.value = EmptyForecast(emptyWithReason.reasonResourceId)
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _todayForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}