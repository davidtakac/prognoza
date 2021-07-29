package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.*
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.*
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.roundToInt

//todo: handle exceptions and http codes
class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    private val placeRepository: PlaceRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private val _todayForecast = MutableLiveData<TodayUiModel>()
    val todayForecast: LiveData<TodayUiModel> get() = _todayForecast

    private val _tomorrowForecast = MutableLiveData<TomorrowUiModel>()
    val tomorrowForecast: LiveData<TomorrowUiModel> get() = _tomorrowForecast

    private val _otherDaysForecast = MutableLiveData<OtherDaysUiModel>()
    val otherDaysForecast: LiveData<OtherDaysUiModel> get() = _otherDaysForecast

    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    private val _isTodayForecastLoading = MutableLiveData(false)
    val isTodayForecastLoading: LiveData<Boolean> get() = _isTodayForecastLoading

    private val _isTomorrowForecastLoading = MutableLiveData(false)
    val isTomorrowForecastLoading: LiveData<Boolean> get() = _isTomorrowForecastLoading

    private val _isOtherDaysForecastLoading = MutableLiveData(false)
    val isOtherDaysForecastLoading: LiveData<Boolean> get() = _isOtherDaysForecastLoading

    fun getTodayForecast() {
        _isTodayForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = forecastRepository.getTodayForecastHours().toHourUiModels()
            val forecastTodayUiModel = TodayUiModel(
                dateTime = ZonedDateTime.now(),
                currentTemperature = uiModels[0].temperature,
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
            val tomorrowHours = forecastRepository.getTomorrowForecastHours()
            val weatherIconAsync = async { tomorrowHours.representativeWeatherIcon() }
            val lowTempAsync = async { tomorrowHours.minTemperature() }
            val highTempAsync = async { tomorrowHours.maxTemperature() }
            val forecastTomorrowUiModel = TomorrowUiModel(
                dateTime = ZonedDateTime.now().atStartOfDay().plusDays(1),
                lowTemperature = lowTempAsync.await(),
                highTemperature = highTempAsync.await(),
                weatherIcon = weatherIconAsync.await(),
                hours = tomorrowHours.toHourUiModels()
            )
            _tomorrowForecast.value = forecastTomorrowUiModel
            _isTomorrowForecastLoading.value = false
        }
    }

    fun getOtherDaysForecast() {
        _isOtherDaysForecastLoading.value = true
        coroutineScope.launch {
            val uiModels = forecastRepository.getAllForecastHours(
                start = ZonedDateTime
                    .now()
                    .atStartOfDay()
                    .plusDays(2)
            ).toDayUiModels()
            _otherDaysForecast.value = OtherDaysUiModel(days = uiModels)
            _isOtherDaysForecastLoading.value = false
        }
    }

    fun getPlaceName() {
        coroutineScope.launch {
            _placeName.value = placeRepository.getSelectedPlace().name
        }
    }

    private suspend fun List<ForecastHour>.maxTemperature() =
        withContext(dispatcherProvider.default) {
            maxOf { it.temperature ?: Float.MIN_VALUE }.roundToInt()
        }

    private suspend fun List<ForecastHour>.minTemperature() =
        withContext(dispatcherProvider.default) {
            minOf { it.temperature ?: Float.MAX_VALUE }.roundToInt()
        }

    private suspend fun List<ForecastHour>.representativeWeatherIcon() =
        withContext(dispatcherProvider.default) {
            val representativeSymbolCode = filter { it.symbolCode != null }
                .map { it.symbolCode!! }
                .filter { it !in NIGHT_SYMBOL_CODES }
                .mostCommon()
            WEATHER_ICONS[representativeSymbolCode]
        }

    private suspend fun List<ForecastHour>.totalPrecipitationAmount() =
        withContext(dispatcherProvider.default) {
            sumOf { it.precipitationAmount?.toDouble() ?: 0.0 }.toFloat()
        }

    private suspend fun List<ForecastHour>.toHourUiModels() =
        withContext(dispatcherProvider.default) {
            map {
                HourUiModel(
                    temperature = it.temperature?.roundToInt(),
                    precipitationAmount = it.precipitationAmount,
                    weatherIcon = WEATHER_ICONS[it.symbolCode],
                    time = it.time
                )
            }
        }

    private suspend fun List<ForecastHour>.toDayUiModels() =
        withContext(dispatcherProvider.default) {
            groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
                .map { it.value }
                .filter { it.isNotEmpty() }
                .map { hours ->
                    val weatherIconAsync = async { hours.representativeWeatherIcon() }
                    val lowTempAsync = async { hours.minTemperature() }
                    val highTempAsync = async { hours.maxTemperature() }
                    val precipitationAsync = async { hours.totalPrecipitationAmount() }
                    DayUiModel(
                        time = hours[0].time,
                        weatherIcon = weatherIconAsync.await(),
                        lowTemperature = lowTempAsync.await(),
                        highTemperature = highTempAsync.await(),
                        precipitationAmount = precipitationAsync.await()
                    )
                }
        }
}