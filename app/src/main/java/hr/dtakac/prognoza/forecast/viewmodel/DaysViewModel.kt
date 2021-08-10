package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.common.util.hasExpired
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.forecast.uimodel.DaysForecast
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId

class DaysViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    private val _daysForecast = MutableLiveData<DaysForecast>()
    val daysForecast: LiveData<DaysForecast> get() = _daysForecast

    private val _emptyScreen = MutableLiveData<EmptyForecast?>()
    val emptyScreen: LiveData<EmptyForecast?> get() = _emptyScreen

    private val _message = MutableLiveData<Event<Int>>()
    val message: LiveData<Event<Int>> get() = _message

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getDaysForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                getNewForecast()
            }
        }
    }

    private suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        when (val result = forecastRepository.getOtherDaysForecastHours(selectedPlaceId)) {
            is ForecastResult.Success -> handleSuccess(result)
            is ForecastResult.Empty -> handleEmpty(result)
        }
        _isLoading.value = false
    }

    private suspend fun handleSuccess(result: ForecastResult.Success) {
        val uiModels = withContext(dispatcherProvider.default) {
            result.hours
                .groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
                .map { it.value }
                .filter { it.isNotEmpty() }
                .map { it.toDayUiModel(this) }
        }
        currentMeta = result.meta
        _daysForecast.value = DaysForecast(uiModels)
    }

    private fun handleEmpty(empty: ForecastResult.Empty) {
        _emptyScreen.value = EmptyForecast(null)
    }

    private suspend fun handleCachedSuccess(cachedResult: ForecastResult.CachedSuccess) {
        handleSuccess(cachedResult.success)
        _message.value = Event(cachedResult.reasonResourceId)
    }

    private suspend fun handleEmptyWithReason(emptyWithReason: ForecastResult.EmptyWithReason) {
        _emptyScreen.value = EmptyForecast(emptyWithReason.reasonResourceId)
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _daysForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}