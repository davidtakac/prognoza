package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.coroutines.DispatcherProvider
import hr.dtakac.prognoza.model.repository.ForecastResult
import hr.dtakac.prognoza.model.repository.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.place.PlaceRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.model.ui.MeasurementUnit
import hr.dtakac.prognoza.model.ui.forecast.TomorrowForecastUiModel
import hr.dtakac.prognoza.common.utils.*
import hr.dtakac.prognoza.common.timeprovider.ForecastTimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TomorrowViewModel(
    coroutineScope: CoroutineScope?,
    preferencesRepository: PreferencesRepository,
    placeRepository: PlaceRepository,
    private val forecastRepository: ForecastRepository,
    private val forecastTimeProvider: ForecastTimeProvider,
    private val dispatcherProvider: DispatcherProvider,
) : ForecastViewModel<TomorrowForecastUiModel>(coroutineScope, preferencesRepository, placeRepository) {
    override val _forecast = MutableLiveData<TomorrowForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        return forecastRepository.getForecastTimeSpans(
            forecastTimeProvider.tomorrowStart,
            forecastTimeProvider.tomorrowEnd,
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