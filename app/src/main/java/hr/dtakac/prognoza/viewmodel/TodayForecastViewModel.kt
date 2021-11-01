package hr.dtakac.prognoza.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.utils.toHourUiModel
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import hr.dtakac.prognoza.utils.timeprovider.ForecastTimeProvider
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
    override val _forecast = MutableLiveData<TodayForecastUiModel>()

    private val _expandedHourIndices = mutableStateListOf<Int>()
    val expandedHourIndices: SnapshotStateList<Int> get() = _expandedHourIndices

    override suspend fun getNewForecast(): ForecastResult {
        return forecastRepository.getForecastTimeSpans(
            forecastTimeProvider.todayStart,
            forecastTimeProvider.todayEnd,
            selectedPlace
        )
    }

    override suspend fun mapToForecastUiModel(
        success: Success,
        unit: MeasurementUnit
    ): TodayForecastUiModel {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans[0].toHourUiModel(unit).copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.map { it.toHourUiModel(unit) }
        }
        return TodayForecastUiModel(
            currentHour = currentHourAsync.await(),
            otherHours = otherHoursAsync.await(),
        )
    }

    fun toggleHour(index: Int) {
        coroutineScope.launch(dispatcherProvider.default) {
            if (index in _expandedHourIndices) {
                _expandedHourIndices.remove(index)
            } else {
                _expandedHourIndices.add(index)
            }
        }
    }
}