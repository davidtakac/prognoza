package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.forecast.uimodel.TomorrowForecastUiModel
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TomorrowFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : BaseForecastFragmentViewModel<TomorrowForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<TomorrowForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        return forecastRepository.getTomorrowForecastHours(selectedPlaceId)
    }

    override suspend fun mapToForecastUiModel(success: Success): TomorrowForecastUiModel {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.toDayUiModel(this)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.map { it.toHourUiModel() }
        }
        return TomorrowForecastUiModel(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
        )
    }
}