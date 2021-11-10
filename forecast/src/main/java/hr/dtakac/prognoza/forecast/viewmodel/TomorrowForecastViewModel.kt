package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.forecast.model.TomorrowForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TomorrowForecastViewModel(
    coroutineScope: CoroutineScope?,
    forecastRepository: ForecastRepository,
    timeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider
) : ForecastViewModel(coroutineScope, timeProvider, forecastRepository) {

    private val _forecast = mutableStateOf<TomorrowForecastUiModel>(TomorrowForecastUiModel.None)
    val forecast: State<TomorrowForecastUiModel> get() = _forecast

    private val _expandedHourIndices = mutableStateListOf<Int>()
    val expandedHourIndices: SnapshotStateList<Int> get() = _expandedHourIndices

    override suspend fun handleSuccess(success: Success) {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.toDayUiModel(coroutineScope = this, place = success.place)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.map { it.toHourUiModel() }
        }
        _forecast.value =  TomorrowForecastUiModel.Success(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
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