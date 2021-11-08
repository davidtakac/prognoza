package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.forecast.mapping.toHourUiModel
import hr.dtakac.prognoza.forecast.model.TodayForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TodayForecastViewModel(
    coroutineScope: CoroutineScope?,
    preferencesRepository: PreferencesRepository,
    placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val forecastTimeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider,
) : ForecastViewModel<TodayForecastUiModel>(coroutineScope, preferencesRepository, placeRepository) {
    override val _forecast = mutableStateOf<TodayForecastUiModel?>(null)

    private val _expandedHourIndices = mutableStateListOf<Int>()
    val expandedHourIndices: SnapshotStateList<Int> get() = _expandedHourIndices

    override suspend fun getNewForecast(): ForecastResult {
        return forecastRepository.getForecastTimeSpans(
            forecastTimeProvider.todayStart,
            forecastTimeProvider.todayEnd,
            selectedPlace
        )
    }

    override suspend fun mapToForecastUiModel(success: Success): TodayForecastUiModel {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans[0].toHourUiModel().copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.map { it.toHourUiModel() }
        }
        return TodayForecastUiModel(
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