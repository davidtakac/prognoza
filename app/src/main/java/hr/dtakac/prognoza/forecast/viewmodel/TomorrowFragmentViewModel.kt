package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecast
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.forecast.ForecastResult
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TomorrowFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : BaseForecastFragmentViewModel(coroutineScope, preferencesRepository) {
    private val _tomorrowForecast = MutableLiveData<TomorrowForecast>()
    val tomorrowForecast: LiveData<TomorrowForecast> get() = _tomorrowForecast

    override suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        when (val result = forecastRepository.getTomorrowForecastHours(selectedPlaceId)) {
            is ForecastResult.Success -> handleSuccess(result)
            is ForecastResult.Empty -> handleEmpty(result)
            is ForecastResult.CachedSuccess -> handleCachedSuccess(result)
        }
        _isLoading.value = false
    }

    override suspend fun handleSuccess(success: ForecastResult.Success) {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.toDayUiModel(this)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.map { it.toHourUiModel() }
        }
        val successUiModel = TomorrowForecast(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
        )
        currentMeta = success.meta
        _tomorrowForecast.value = successUiModel
        _emptyScreen.value = null
    }

    override suspend fun isReloadNeeded(): Boolean {
        return super.isReloadNeeded() || _tomorrowForecast.value == null
    }
}