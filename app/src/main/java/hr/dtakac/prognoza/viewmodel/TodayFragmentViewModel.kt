package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.extensions.toHourUiModel
import hr.dtakac.prognoza.repomodel.ForecastResult
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.forecast.TodayForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.ZonedDateTime

class TodayFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : ForecastFragmentViewModel<TodayForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<TodayForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        return forecastRepository.getTodayForecastTimeSpans(selectedPlaceId)
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
}