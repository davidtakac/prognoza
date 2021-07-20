package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.GMT_ZONE_ID
import hr.dtakac.prognoza.WEATHER_ICONS
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import hr.dtakac.prognoza.forecast.uimodel.TodayUiModel
import hr.dtakac.prognoza.forecast.uimodel.TomorrowUiModel
import hr.dtakac.prognoza.getTomorrow
import hr.dtakac.prognoza.mostCommon
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private val _todayForecast = MutableLiveData<TodayUiModel>()
    val todayForecast: LiveData<TodayUiModel> get() = _todayForecast

    private val _tomorrowForecast = MutableLiveData<TomorrowUiModel>()
    val tomorrowForecast: LiveData<TomorrowUiModel> get() = _tomorrowForecast

    private val _isTodayForecastLoading = MutableLiveData(false)
    val isTodayForecastLoading: LiveData<Boolean> get() = _isTodayForecastLoading

    private val _isTomorrowForecastLoading = MutableLiveData(false)
    val isTomorrowForecastLoading: LiveData<Boolean> get() = _isTomorrowForecastLoading

    fun getTodayForecast() {
        _isTodayForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = mapToHourUiModels(forecastRepository.getRestOfDayForecastHours())
            val forecastTodayUiModel = TodayUiModel(
                dateTime = ZonedDateTime.now(),
                currentTemperature = uiModels[0].temperature.roundToInt().toShort(),
                weatherIcon = uiModels[0].weatherIcon,
                nextHours = uiModels
            )
            _todayForecast.value = forecastTodayUiModel
            _isTodayForecastLoading.value = false
        }
    }

    fun getTomorrowForecast() {
        _isTomorrowForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = mapToHourUiModels(forecastRepository.getTomorrowForecastHours())
            val forecastTomorrowUiModel = TomorrowUiModel(
                dateTime = getTomorrow(),
                lowTemperature = uiModels.getLowestTemperature().roundToInt().toShort(),
                highTemperature = uiModels.getHighestTemperature().roundToInt().toShort(),
                weatherIcon = uiModels.getMostCommonWeatherIcon(),
                hours = uiModels
            )
            _tomorrowForecast.value = forecastTomorrowUiModel
            _isTomorrowForecastLoading.value = false
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
                    dateTimeGmt = ZonedDateTime
                        .parse(it.timestamp, DateTimeFormatter.ISO_DATE_TIME)
                        .withZoneSameInstant(GMT_ZONE_ID)
                )
            }
    }

    private suspend fun List<HourUiModel>.getLowestTemperature() =
        withContext(dispatcherProvider.default) {
            minOf { it.temperature }
        }

    private suspend fun List<HourUiModel>.getHighestTemperature() =
        withContext(dispatcherProvider.default) {
            maxOf { it.temperature }
        }

    private suspend fun List<HourUiModel>.getMostCommonWeatherIcon() =
        withContext(dispatcherProvider.default) {
            map { it.weatherIcon }.mostCommon()
        }
}