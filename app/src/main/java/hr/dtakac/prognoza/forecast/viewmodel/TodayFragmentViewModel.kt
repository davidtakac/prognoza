package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.common.util.totalPrecipitationAmount
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.forecast.uimodel.TodayForecast
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.ZonedDateTime

class TodayFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : BaseForecastFragmentViewModel(coroutineScope, preferencesRepository) {
    private val _todayForecast = MutableLiveData<TodayForecast>()
    val todayForecast: LiveData<TodayForecast> get() = _todayForecast

    override suspend fun getNewForecast() {
        _isLoading.value = true
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        when (val result = forecastRepository.getTodayForecastHours(selectedPlaceId)) {
            is Success -> handleSuccess(result)
            is Empty -> handleEmpty(result)
            is CachedSuccess -> handleCachedSuccess(result)
        }
        _isLoading.value = false
    }

    override suspend fun handleSuccess(success: Success) {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours[0].toHourUiModel().copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.map { it.toHourUiModel() }
        }
        val precipitationForecastAsync = coroutineScope.async(dispatcherProvider.default) {
            val total = success.hours.subList(0, 2).totalPrecipitationAmount()
            if (total <= 0f) null else total
        }
        val forecastTodayUiModel = TodayForecast(
            currentHour = currentHourAsync.await(),
            otherHours = otherHoursAsync.await(),
            precipitationForecast = precipitationForecastAsync.await()
        )
        currentMeta = success.meta
        _todayForecast.value = forecastTodayUiModel
        _emptyScreen.value = null
    }

    override suspend fun isReloadNeeded(): Boolean {
        return super.isReloadNeeded() || _todayForecast.value == null
    }
}