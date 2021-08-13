package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.common.util.hasExpired
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecast
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseForecastFragmentViewModel(
    coroutineScope: CoroutineScope?,
    protected val preferencesRepository: PreferencesRepository
): CoroutineScopeViewModel(coroutineScope) {
    protected var currentMeta: ForecastMeta? = null

    protected val _emptyScreen = MutableLiveData<EmptyForecast?>()
    val emptyScreen: LiveData<EmptyForecast?> get() = _emptyScreen

    protected val _message = MutableLiveData<Event<Int>>()
    val message: LiveData<Event<Int>> get() = _message

    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    open fun getForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                getNewForecast()
            }
        }
    }

    protected abstract suspend fun getNewForecast()

    protected abstract suspend fun handleSuccess(success: ForecastResult.Success)

    protected open fun handleEmpty(empty: ForecastResult.Empty) {
        _emptyScreen.value = EmptyForecast(null)
    }

    protected open suspend fun handleCachedSuccess(cachedResult: ForecastResult.CachedSuccess) {
        handleSuccess(cachedResult.success)
        _message.value = Event(R.string.notify_cached_result)
    }

    protected open fun handleEmptyWithReason(emptyWithReason: ForecastResult.EmptyWithReason) {
        _emptyScreen.value = EmptyForecast(emptyWithReason.reasonResourceId)
    }

    protected open suspend fun isReloadNeeded(): Boolean {
        return currentMeta?.hasExpired() != false ||
                currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}