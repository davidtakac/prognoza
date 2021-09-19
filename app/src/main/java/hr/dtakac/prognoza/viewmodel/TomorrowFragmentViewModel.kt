package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TomorrowFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository,
    placeRepository: PlaceRepository
) : ForecastFragmentViewModel<TomorrowForecastUiModel>(coroutineScope, preferencesRepository, placeRepository) {
    override val _forecast = MutableLiveData<TomorrowForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        return forecastRepository.getForecastTimeSpans(
            forecastStartOfTomorrow,
            forecastEndOfTomorrow,
            selectedPlace
        )
    }

    override suspend fun mapToForecastUiModel(
        success: Success,
        unit: MeasurementUnit
    ): TomorrowForecastUiModel {
        val summaryAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.toDayUiModel(this, unit, selectedPlace)
        }
        val hoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.timeSpans.map { it.toHourUiModel(unit) }
        }
        return TomorrowForecastUiModel(
            summary = summaryAsync.await(),
            hours = hoursAsync.await()
        )
    }
}