package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.common.util.toHourUiModel
import hr.dtakac.prognoza.common.util.totalPrecipitationAmount
import hr.dtakac.prognoza.coroutines.DispatcherProvider
import hr.dtakac.prognoza.forecast.uimodel.TodayForecastUiModel
import hr.dtakac.prognoza.repository.forecast.*
import hr.dtakac.prognoza.repository.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.time.ZonedDateTime

class TodayFragmentViewModel(
    coroutineScope: CoroutineScope?,
    private val forecastRepository: ForecastRepository,
    private val dispatcherProvider: DispatcherProvider,
    preferencesRepository: PreferencesRepository
) : BaseForecastFragmentViewModel<TodayForecastUiModel>(coroutineScope, preferencesRepository) {
    override val _forecast = MutableLiveData<TodayForecastUiModel>()

    override suspend fun getNewForecast(): ForecastResult {
        val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
        return forecastRepository.getTodayForecastHours(selectedPlaceId)
    }

    override suspend fun mapToForecastUiModel(success: Success): TodayForecastUiModel {
        val currentHourAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours[0].toHourUiModel().copy(time = ZonedDateTime.now())
        }
        val otherHoursAsync = coroutineScope.async(dispatcherProvider.default) {
            success.hours.map { it.toHourUiModel() }
        }
        val precipitationForecastAsync = coroutineScope.async(dispatcherProvider.default) {
            val total = success.hours.subList(0, 2).totalPrecipitationAmount()
            if (total <= 0f) null else total
        }
        return TodayForecastUiModel(
            currentHour = currentHourAsync.await(),
            otherHours = otherHoursAsync.await(),
            precipitationForecast = precipitationForecastAsync.await()
        )
    }
}