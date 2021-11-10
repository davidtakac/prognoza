package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.ForecastTimeSpan
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
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
    preferencesRepository: PreferencesRepository,
    placeRepository: PlaceRepository,
    metaRepository: MetaRepository,
    private val forecastRepository: ForecastRepository,
    private val forecastTimeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider,
) : ForecastViewModel<ComingForecastUiModel>(
    coroutineScope,
    preferencesRepository,
    placeRepository,
    metaRepository
) {
    override val _forecast = mutableStateOf<ComingForecastUiModel?>(null)

    private val _expandedDayIndices = mutableStateListOf<Int>()
    val expandedDayIndices: SnapshotStateList<Int> get() = _expandedDayIndices

    override suspend fun getNewForecast(
        place: Place,
        meta: ForecastMeta?
    ): ForecastResult {
        return forecastRepository.updateForecastTimespans(
            start = forecastTimeProvider.comingStart,
            end = forecastTimeProvider.comingEnd,
            place = place,
            placeMeta = meta
        )
    }

    override suspend fun mapToForecastUiModel(
        success: Success,
        place: Place
    ): ComingForecastUiModel {
        return ComingForecastUiModel(
            days = withContext(dispatcherProvider.default) {
                var endOfDay: ZonedDateTime = forecastTimeProvider.tomorrowEnd
                val dayHours: MutableList<ForecastTimeSpan> = mutableListOf()
                val summaries: MutableList<DayUiModel> = mutableListOf()
                for (i in success.timeSpans.indices) {
                    val currentTimeSpan = success.timeSpans[i]
                    if (currentTimeSpan.startTime < endOfDay) {
                        dayHours.add(currentTimeSpan)
                    } else {
                        summaries.add(
                            dayHours.toDayUiModel(
                                coroutineScope = this,
                                place = place
                            )
                        )
                        dayHours.clear()
                        dayHours.add(currentTimeSpan)
                        val hoursLeftInDay = 24 - currentTimeSpan.startTime.hour
                        endOfDay =
                            currentTimeSpan.startTime.plusHours(hoursLeftInDay + HOURS_AFTER_MIDNIGHT)
                    }
                }
                summaries
            }
        )
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