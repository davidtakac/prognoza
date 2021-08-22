package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.extensions.toDayUiModel
import hr.dtakac.prognoza.extensions.toHourUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.uimodel.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TomorrowFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : ForecastFragmentViewModel<TomorrowForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<TomorrowForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        return forecastRepository.getTomorrowForecastHours(selectedPlaceId)
    }

    override suspend fun mapToForecastUiModel(success: Success, unit: MeasurementUnit): TomorrowForecastUiModel {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.toDayUiModel(this, unit)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.map { it.toHourUiModel(unit) }
        }
        return TomorrowForecastUiModel(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
        )
    }
}