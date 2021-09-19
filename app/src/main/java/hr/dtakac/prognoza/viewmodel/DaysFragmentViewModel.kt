package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.HOURS_AFTER_MIDNIGHT
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.extensions.atStartOfDay
import hr.dtakac.prognoza.extensions.toDayUiModel
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import hr.dtakac.prognoza.uimodel.forecast.DaysForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class DaysFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    preferencesRepository: PreferencesRepository
) : ForecastFragmentViewModel<DaysForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<DaysForecastUiModel>()

    private val start = ZonedDateTime
        .now()
        .atStartOfDay()
        .plusDays(1L)
        .plusHours(HOURS_AFTER_MIDNIGHT + 1L)

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        val end = start
            .plusDays(7L)
            .minusHours(1L)
        return forecastRepository.getForecastTimeSpans(start, end, selectedPlaceId)
    }

    override suspend fun mapToForecastUiModel(
        success: Success,
        unit: MeasurementUnit
    ): DaysForecastUiModel {
        return DaysForecastUiModel(
            days = withContext(dispatcherProvider.default) {
                var endOfDay: ZonedDateTime = start
                    .plusDays(1L)
                    .atStartOfDay()
                    .plusHours(HOURS_AFTER_MIDNIGHT)
                val dayHours: MutableList<ForecastTimeSpan> = mutableListOf()
                val summaries: MutableList<DayUiModel> = mutableListOf()
                for (i in success.timeSpans.indices) {
                    val currentTimeSpan = success.timeSpans[i]
                    if (currentTimeSpan.startTime < endOfDay) {
                        dayHours.add(currentTimeSpan)
                    } else {
                        summaries.add(dayHours.toDayUiModel(this, unit))
                        dayHours.clear()
                        dayHours.add(currentTimeSpan)
                        endOfDay = currentTimeSpan.startTime
                            .plusDays(1L)
                            .atStartOfDay()
                            .plusHours(HOURS_AFTER_MIDNIGHT)
                    }
                }
                summaries
            }
        )
    }
}