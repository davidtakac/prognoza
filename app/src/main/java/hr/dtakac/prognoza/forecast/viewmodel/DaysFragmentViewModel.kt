package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.forecast.uimodel.DaysForecast
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.time.ZoneId

class DaysFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    preferencesRepository: PreferencesRepository
) : BaseForecastFragmentViewModel(coroutineScope, preferencesRepository) {
    private val _daysForecast = MutableLiveData<DaysForecast>()
    val daysForecast: LiveData<DaysForecast> get() = _daysForecast

    override suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        when (val result = forecastRepository.getOtherDaysForecastHours(selectedPlaceId)) {
            is Success -> handleSuccess(result)
            is Empty -> handleEmpty(result)
            is CachedSuccess -> handleCachedSuccess(result)
        }
        _isLoading.value = false
    }

    override suspend fun handleSuccess(success: Success) {
        val uiModels = withContext(dispatcherProvider.default) {
            success.hours
                .groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
                .map { it.value }
                .filter { it.isNotEmpty() }
                .map { it.toDayUiModel(this) }
        }
        currentMeta = success.meta
        _daysForecast.value = DaysForecast(uiModels)
    }

    override suspend fun isReloadNeeded(): Boolean {
        return super.isReloadNeeded() || _daysForecast.value == null
    }
}