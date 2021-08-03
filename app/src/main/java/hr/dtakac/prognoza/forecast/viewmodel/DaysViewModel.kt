package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.database.entity.hasExpired
import hr.dtakac.prognoza.database.entity.toDayUiModels
import hr.dtakac.prognoza.forecast.uimodel.DaysUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DaysViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    private val _daysForecast = MutableLiveData<DaysUiModel>()
    val daysForecast: LiveData<DaysUiModel> get() = _daysForecast

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
            is ForecastResult.Error -> handleError(result)
        }
        _isLoading.value = false
    }

    private suspend fun handleSuccess(result: ForecastResult.Success) {
        val uiModels = withContext(dispatcherProvider.default) {
            result.hours.toDayUiModels(this)
        }
        currentMeta = result.meta
        _daysForecast.value = DaysUiModel.Success(days = uiModels)
    }

    private fun handleError(result: ForecastResult.Error) {
        _daysForecast.value = DaysUiModel.Error(result.errorMessageResourceId)
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _daysForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}