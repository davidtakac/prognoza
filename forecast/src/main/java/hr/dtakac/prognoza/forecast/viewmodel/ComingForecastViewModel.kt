package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.core.timeprovider.TomorrowForecastTimeProvider
import hr.dtakac.prognoza.core.utils.HOURS_AFTER_MIDNIGHT
import hr.dtakac.prognoza.forecast.mapping.toDayUiModel
import hr.dtakac.prognoza.forecast.model.DayUiModel
import hr.dtakac.prognoza.forecast.model.ComingForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

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
        val days = withContext(dispatcherProvider.default) {
            var endOfDay: ZonedDateTime = tomorrowForecastTimeProvider.end
            val dayHours: MutableList<ForecastTimeSpan> = mutableListOf()
            val days: MutableList<DayUiModel> = mutableListOf()
            for (i in success.timeSpans.indices) {
                val currentTimeSpan = success.timeSpans[i]
                if (currentTimeSpan.startTime < endOfDay) {
                    dayHours.add(currentTimeSpan)
                } else {
                    days.add(
                        dayHours.toDayUiModel(
                            coroutineScope = this,
                            place = success.place
                        )
                    )
                    dayHours.clear()
                    dayHours.add(currentTimeSpan)
                    val hoursLeftInDay = 24 - currentTimeSpan.startTime.hour
                    endOfDay = currentTimeSpan.startTime.plusHours(hoursLeftInDay + HOURS_AFTER_MIDNIGHT)
                }
            }
            days
        }
        _forecast.value = ComingForecastUiModel.Success(days = days)
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