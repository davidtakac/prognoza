package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.HOURS_AFTER_MIDNIGHT
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import hr.dtakac.prognoza.uimodel.forecast.DaysForecastUiModel
import hr.dtakac.prognoza.utils.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.utils.toDayUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class DaysFragmentViewModel(
    coroutineScope: CoroutineScope?,
    preferencesRepository: PreferencesRepository,
    placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val forecastTimeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider,
) : ForecastFragmentViewModel<DaysForecastUiModel>(
    coroutineScope,
    preferencesRepository,
    placeRepository
) {
    override val _forecast = MutableLiveData<DaysForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        return forecastRepository.getForecastTimeSpans(
            forecastTimeProvider.comingStart,
            forecastTimeProvider.comingEnd,
            selectedPlace
        )
    }

    override suspend fun mapToForecastUiModel(
        success: Success,
        unit: MeasurementUnit
    ): DaysForecastUiModel {
        return DaysForecastUiModel(
            days = withContext(dispatcherProvider.default) {
                var endOfDay: ZonedDateTime = forecastTimeProvider.tomorrowEnd
                val dayHours: MutableList<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan> = mutableListOf()
                val summaries: MutableList<DayUiModel> = mutableListOf()
                for (i in success.timeSpans.indices) {
                    val currentTimeSpan = success.timeSpans[i]
                    if (currentTimeSpan.startTime < endOfDay) {
                        dayHours.add(currentTimeSpan)
                    } else {
                        summaries.add(dayHours.toDayUiModel(this, unit, selectedPlace))
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
}