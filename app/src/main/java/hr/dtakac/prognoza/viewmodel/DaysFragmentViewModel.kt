package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.extensions.toDayUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.uimodel.forecast.DaysForecastUiModel
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.time.ZoneId

class DaysFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val dispatcherProvider: DispatcherProvider,
    private val forecastRepository: ForecastRepository,
    preferencesRepository: PreferencesRepository
) : ForecastFragmentViewModel<DaysForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<DaysForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        return forecastRepository.getOtherDaysForecastHours(selectedPlaceId)
    }

    override suspend fun mapToForecastUiModel(success: Success, unit: MeasurementUnit): DaysForecastUiModel {
        val daySummaries = withContext(dispatcherProvider.default) {
            success.hours
                .groupBy { it.time.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
                .map { it.value }
                .filter { it.isNotEmpty() }
                .map { it.toDayUiModel(this, unit) }

        }
        return DaysForecastUiModel(daySummaries)
    }
}