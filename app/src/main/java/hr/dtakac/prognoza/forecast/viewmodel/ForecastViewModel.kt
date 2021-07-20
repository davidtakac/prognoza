package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.WEATHER_ICONS
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import hr.dtakac.prognoza.forecast.uimodel.TodayUiModel
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private val _forecastData = MutableLiveData<TodayUiModel>()
    val forecastData: LiveData<TodayUiModel> get() = _forecastData

    private val _isForecastLoading = MutableLiveData<Boolean>()
    val isForecastLoading: LiveData<Boolean> get() = _isForecastLoading

    fun getTodayForecast() {
        _isForecastLoading.value = true
        coroutineScope.launch {
            val forecastHourUiModels = mapToHourUiModels(forecastRepository.getRestOfDayForecastHours())
            val currentForecastHour = forecastHourUiModels[0]
            val forecastTodayUiModel = TodayUiModel(
                dateTime = LocalDateTime.now(),
                currentTemperature = currentForecastHour.temperature.roundToInt().toShort(),
                weatherIcon = currentForecastHour.weatherIcon,
                nextHours = forecastHourUiModels
            )
            _forecastData.value = forecastTodayUiModel
            _isForecastLoading.value = false
        }
    }

    private suspend fun mapToHourUiModels(forecastHours: List<ForecastHour>) = withContext(dispatcherProvider.default) {
        forecastHours
            .filter { it.temperature != null && it.symbolCode != null }
            .map {
                HourUiModel(
                    temperature = it.temperature!!,
                    precipitationAmount = it.precipitationAmount,
                    weatherIcon = WEATHER_ICONS[it.symbolCode!!]!!,
                    dateTime = LocalDateTime.parse(it.dateTime, DateTimeFormatter.ISO_DATE_TIME)
                )
            }
    }
}