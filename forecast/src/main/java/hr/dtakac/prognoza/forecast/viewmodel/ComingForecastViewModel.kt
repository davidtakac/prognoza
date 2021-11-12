package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.core.timeprovider.TomorrowForecastTimeProvider
import hr.dtakac.prognoza.forecast.mapping.toInstantUiModel
import hr.dtakac.prognoza.forecast.model.ComingForecastUiModel
import hr.dtakac.prognoza.forecast.model.InstantUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ComingForecastViewModel(
    coroutineScope: CoroutineScope?,
    forecastRepository: ForecastRepository,
    timeProvider: ForecastTimeProvider,
    private val tomorrowForecastTimeProvider: TomorrowForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider
) : ForecastViewModel(coroutineScope, timeProvider, forecastRepository){

    private val _forecast = mutableStateOf<ComingForecastUiModel>(ComingForecastUiModel.None)
    val forecast: State<ComingForecastUiModel> get() = _forecast

    private val _expandedDayIndices = mutableStateListOf<Int>()
    val expandedDayIndices: SnapshotStateList<Int> get() = _expandedDayIndices

    override suspend fun handleSuccess(success: ForecastResult.Success) {
        val instants = coroutineScope.async(dispatcherProvider.default) {
            mutableListOf<InstantUiModel>().apply {
                for (i in success.timeSpans.indices) {
                    add(success.timeSpans[i].toInstantUiModel(success.timeSpans.getOrNull(i + 1)))
                }
            }
        }
        _forecast.value = ComingForecastUiModel.Success(instants = instants.await())
    }

    fun toggleExpanded(index: Int) {
        coroutineScope.launch(dispatcherProvider.default) {
            if (index in _expandedDayIndices) {
                _expandedDayIndices.remove(index)
            } else {
                _expandedDayIndices.add(index)
            }
        }
    }
}