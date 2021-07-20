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

    private val _isTodayForecastLoading = MutableLiveData<Boolean>()
    val isTodayForecastLoading: LiveData<Boolean> get() = _isTodayForecastLoading

    private val _isTomorrowForecastLoading = MutableLiveData<Boolean>()
    val isTomorrowForecastLoading: LiveData<Boolean> get() = _isTomorrowForecastLoading

    fun getTodayForecast() {
        _isTodayForecastLoading.value = true
        coroutineScope.launch {
            val forecastHourUiModels = mapToHourUiModels(forecastRepository.getRestOfDayForecastHours())
            val forecastTodayUiModel = TodayUiModel(
                dateTime = ZonedDateTime.now(),
                currentTemperature = forecastHourUiModels[0].temperature.roundToInt().toShort(),
                weatherIcon = forecastHourUiModels[0].weatherIcon,
                nextHours = forecastHourUiModels
            )
            _todayForecast.value = forecastTodayUiModel
            _isTodayForecastLoading.value = false
        }
    }

    fun getTomorrowForecast() {
        _isTomorrowForecastLoading.value = true
        coroutineScope.launch {
            val forecastHourUiModels = mapToHourUiModels(forecastRepository.getTomorrowForecastHours())
            val lowTemperature = withContext(dispatcherProvider.default) {
                forecastHourUiModels.minOf { it.temperature }
            }
            val highTemperature = withContext(dispatcherProvider.default) {
                forecastHourUiModels.maxOf { it.temperature }
            }
            val representativeWeatherIcon = forecastHourUiModels
                .map { it.weatherIcon }
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }!!
                .key
            val forecastTomorrowUiModel = TomorrowUiModel(
                dateTime = getTomorrow(),
                lowTemperature = lowTemperature.roundToInt().toShort(),
                highTemperature = highTemperature.roundToInt().toShort(),
                weatherIcon = representativeWeatherIcon,
                hours = forecastHourUiModels
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
}