package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.common.util.hasExpired
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecastUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TomorrowViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val preferencesRepository: PreferencesRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    private val _tomorrowForecast = MutableLiveData<TomorrowForecastUiModel>()
    val tomorrowForecast: LiveData<TomorrowForecastUiModel> get() = _tomorrowForecast

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
        when (val result = forecastRepository.getTomorrowForecastHours(selectedPlaceId)) {
            is ForecastResult.Success -> handleSuccess(result)
            is ForecastResult.Error -> handleError(result)
        }
        _isLoading.value = false
    }

    private suspend fun handleSuccess(result: ForecastResult.Success) {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            result.hours.toDayUiModel(this)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            result.hours.map { it.toHourUiModel() }
        }
        val successUiModel = TomorrowForecastUiModel.Success(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
        )
        currentMeta = result.meta
        _tomorrowForecast.value = successUiModel
    }

    private fun handleError(error: ForecastResult.Error) {
        _tomorrowForecast.value = TomorrowForecastUiModel.Error(error.errorMessageResourceId)
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _tomorrowForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}