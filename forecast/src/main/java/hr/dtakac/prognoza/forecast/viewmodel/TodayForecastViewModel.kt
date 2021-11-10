package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.forecast.mapping.toHourUiModel
import hr.dtakac.prognoza.forecast.model.TodayForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TodayForecastViewModel(
    coroutineScope: CoroutineScope?,
    forecastRepository: ForecastRepository,
    timeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider
) : ForecastViewModel(coroutineScope, timeProvider, forecastRepository) {

    private val _forecast = mutableStateOf<TodayForecastUiModel>(TodayForecastUiModel.None)
    val forecast: State<TodayForecastUiModel> get() = _forecast

    private val _expandedHourIndices = mutableStateListOf<Int>()
    val expandedHourIndices: SnapshotStateList<Int> get() = _expandedHourIndices

    override suspend fun handleSuccess(success: Success) {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans[0].toHourUiModel().copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.map { it.toHourUiModel() }
        }
        _forecast.value =  TodayForecastUiModel.Success(
            currentHour = currentHourAsync.await(),
            otherHours = otherHoursAsync.await(),
        )
    }

    fun toggleExpanded(index: Int) {
        coroutineScope.launch(dispatcherProvider.default) {
            if (index in _expandedHourIndices) {
                _expandedHourIndices.remove(index)
            } else {
                _expandedHourIndices.add(index)
            }
        }
    }
}