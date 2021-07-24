package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.*
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.roundToInt

//todo: handle exceptions and http codes
class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val repo: ForecastRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private val _todayForecast = MutableLiveData<TodayUiModel>()
    val todayForecast: LiveData<TodayUiModel> get() = _todayForecast

    private val _tomorrowForecast = MutableLiveData<TomorrowUiModel>()
    val tomorrowForecast: LiveData<TomorrowUiModel> get() = _tomorrowForecast

    private val _otherDaysForecast = MutableLiveData<OtherDaysUiModel>()
    val otherDaysForecast: LiveData<OtherDaysUiModel> get() = _otherDaysForecast

    private val _isTodayForecastLoading = MutableLiveData(false)
    val isTodayForecastLoading: LiveData<Boolean> get() = _isTodayForecastLoading

    private val _isTomorrowForecastLoading = MutableLiveData(false)
    val isTomorrowForecastLoading: LiveData<Boolean> get() = _isTomorrowForecastLoading

    private val _isOtherDaysForecastLoading = MutableLiveData(false)
    val isOtherDaysForecastLoading: LiveData<Boolean> get() = _isOtherDaysForecastLoading

    fun getTodayForecast() {
        _isTodayForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = repo.getTodayForecastHours().toHourUiModels()
            val forecastTodayUiModel = TodayUiModel(
                dateTime = ZonedDateTime.now(),
                currentTemperature = uiModels[0].temperature.roundToInt(),
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
            val tomorrowHours = repo.getTomorrowForecastHours()
            val uiModels = tomorrowHours.toHourUiModels()
            val forecastTomorrowUiModel = TomorrowUiModel(
                dateTime = ZonedDateTime.now().atStartOfDay().plusDays(1),
                lowTemperature = tomorrowHours.minTemperature(),
                highTemperature = tomorrowHours.maxTemperature(),
                weatherIcon = tomorrowHours.representativeWeatherIcon(),
                hours = uiModels
            )
            _tomorrowForecast.value = forecastTomorrowUiModel
            _isTomorrowForecastLoading.value = false
        }
    }

    fun getOtherDaysForecast() {
        _isOtherDaysForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = repo.getAllForecastHours(
                start = ZonedDateTime
                    .now()
                    .atStartOfDay()
                    .plusDays(2)
            ).toDayUiModels()
            _otherDaysForecast.value = OtherDaysUiModel(days = uiModels)
            _isOtherDaysForecastLoading.value = false
        }
    }

    private suspend fun List<ForecastHour>.filterInvalidHours() =
        withContext(dispatcherProvider.default) {
            filter { it.temperature != null && it.symbolCode != null }
        }

    private suspend fun List<ForecastHour>.maxTemperature() =
        withContext(dispatcherProvider.default) {
            maxOf { it.temperature!! }.roundToInt()
        }

    private suspend fun List<ForecastHour>.minTemperature() =
        withContext(dispatcherProvider.default) {
            minOf { it.temperature!! }.roundToInt()
        }

    private suspend fun List<ForecastHour>.representativeWeatherIcon(
        default: String = DEFAULT_SYMBOL_CODE
    ): WeatherIcon {
        return withContext(dispatcherProvider.default) {
            val representativeSymbolCode = filter { it.symbolCode != null }
                .map { it.symbolCode!! }
                .filter { it !in NIGHT_SYMBOL_CODES }
                .mostCommon() ?: default
            WEATHER_ICONS[representativeSymbolCode]!!
        }
    }

    private suspend fun List<ForecastHour>.totalPrecipitationAmount() =
        withContext(dispatcherProvider.default) {
            sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()
        }

    private suspend fun List<ForecastHour>.toHourUiModels() =
        withContext(dispatcherProvider.default) {
            filterInvalidHours()
                .map {
                    HourUiModel(
                        temperature = it.temperature!!,
                        precipitationAmount = it.precipitationAmount,
                        weatherIcon = WEATHER_ICONS[it.symbolCode!!]!!,
                        time = it.time
                    )
                }
        }

    private suspend fun List<ForecastHour>.toDayUiModels() =
        withContext(dispatcherProvider.default) {
            groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
                .map { it.value.filterInvalidHours() }
                .filter { it.isNotEmpty() }
                .map { hours ->
                    DayUiModel(
                        time = hours[0].time,
                        weatherIcon = hours.representativeWeatherIcon(),
                        lowTemperature = hours.minTemperature(),
                        highTemperature = hours.maxTemperature(),
                        precipitationAmount = hours.totalPrecipitationAmount()
                    )
                }
        }
}