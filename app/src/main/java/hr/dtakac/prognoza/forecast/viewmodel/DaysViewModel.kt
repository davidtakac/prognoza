package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastMeta
import hr.dtakac.prognoza.database.entity.hasExpired
import hr.dtakac.prognoza.database.entity.toDayUiModels
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
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

    private val _daysForecast = MutableLiveData<OtherDaysUiModel>()
    val daysForecast: LiveData<OtherDaysUiModel> get() = _daysForecast

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getDaysForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                _isLoading.value = true
                val forecastHours =
                    forecastRepository.getOtherDaysForecastHours(preferencesRepository.getSelectedPlaceId())
                val uiModels = withContext(dispatcherProvider.default) {
                    forecastHours.hours.toDayUiModels(this)
                }
                currentMeta = forecastHours.meta
                _daysForecast.value = OtherDaysUiModel(days = uiModels)
                _isLoading.value = false
            }
        }
    }

    private suspend fun isReloadNeeded(): Boolean {
        return _daysForecast.value == null
                || currentMeta?.hasExpired() != false
                || currentMeta?.placeId != preferencesRepository.getSelectedPlaceId()
    }
}