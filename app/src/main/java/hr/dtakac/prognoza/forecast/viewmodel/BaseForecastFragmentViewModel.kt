package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.base.Event
import hr.dtakac.prognoza.common.util.hasExpired
import hr.dtakac.prognoza.common.util.toErrorResourceId
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.forecast.uimodel.EmptyForecastUiModel
import hr.dtakac.prognoza.forecast.uimodel.ForecastUiModel
import hr.dtakac.prognoza.repository.forecast.CachedSuccess
import hr.dtakac.prognoza.repository.forecast.Empty
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.forecast.Success
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseForecastFragmentViewModel<T: ForecastUiModel>(
    coroutineScope: CoroutineScope?,
    protected val preferencesRepository: PreferencesRepository
): CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    abstract val _forecast: MutableLiveData<T>
    val forecast: LiveData<T> get() = _forecast

    private val _emptyScreen = MutableLiveData<EmptyForecastUiModel?>()
    val emptyScreen: LiveData<EmptyForecastUiModel?> get() = _emptyScreen

    private val _cachedResultsMessage = MutableLiveData<Event<Int?>>()
    val cachedResultsMessage: LiveData<Event<Int?>> get() = _cachedResultsMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                _isLoading.value = true
                when (val result = getNewForecast()) {
                    is Success -> handleSuccess(result)
                    is CachedSuccess -> handleCachedSuccess(result)
                    is Empty -> handleEmpty(result)
                }
                _isLoading.value = false
            }
        }
    }

    protected abstract suspend fun getNewForecast(): ForecastResult

    protected abstract suspend fun mapToForecastUiModel(success: Success): T

    private suspend fun handleSuccess(success: Success) {
        _forecast.value = mapToForecastUiModel(success)
        currentMeta = success.meta
        _emptyScreen.value = null
    }

    private fun handleEmpty(empty: Empty) {
        _emptyScreen.value = EmptyForecastUiModel(empty.reason?.toErrorResourceId())
    }

    private suspend fun handleCachedSuccess(cachedResult: CachedSuccess) {
        handleSuccess(cachedResult.success)
        _cachedResultsMessage.value = Event(cachedResult.reason?.toErrorResourceId())
    }

    private suspend fun isReloadNeeded(): Boolean {
        return currentMeta?.hasExpired() != false ||
                currentMeta?.placeId != preferencesRepository.getSelectedPlaceId() ||
                _forecast.value == null
    }
}