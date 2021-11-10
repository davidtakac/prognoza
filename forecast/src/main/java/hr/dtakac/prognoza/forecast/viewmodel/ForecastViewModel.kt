package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.*
import hr.dtakac.prognoza.core.model.repository.*
import hr.dtakac.prognoza.core.repository.forecast.ForecastRepository
import hr.dtakac.prognoza.core.timeprovider.ForecastTimeProvider
import hr.dtakac.prognoza.core.utils.ProgressTimeLatch
import hr.dtakac.prognoza.core.utils.toErrorResourceId
import hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel
import hr.dtakac.prognoza.forecast.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

abstract class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val timeProvider: ForecastTimeProvider,
    private val forecastRepository: ForecastRepository
) : CoroutineScopeViewModel(coroutineScope) {

    private val _emptyForecast = mutableStateOf<EmptyForecastUiModel?>(null)
    val emptyForecast: State<EmptyForecastUiModel?> get() = _emptyForecast

    private val _outdatedForecast = mutableStateOf<OutdatedForecastUiModel?>(null)
    val outdatedForecast: State<OutdatedForecastUiModel?> get() = _outdatedForecast

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val progressTimeLatch = ProgressTimeLatch {
        _isLoading.value = it
    }

    init {
        this.coroutineScope.launch {
            forecastRepository.result.collect {
                when (it) {
                    is ForecastResult.Success -> {
                        _emptyForecast.value = null
                        handleSuccess(it)
                    }
                    is ForecastResult.Cached -> handleCachedSuccess(it)
                    is ForecastResult.Empty -> handleEmpty(it)
                    is ForecastResult.None -> handleNone()
                }
            }
        }
    }

    abstract suspend fun handleSuccess(success: ForecastResult.Success)

    fun getForecast() {
        coroutineScope.launch {
            progressTimeLatch.loading = true
            forecastRepository.updateForecastResult(
                start = timeProvider.start,
                end = timeProvider.end
            )
            progressTimeLatch.loading = false
        }
    }

    private fun handleEmpty(empty: ForecastResult.Empty) {
        _emptyForecast.value = if (empty.reason is ForecastError.NoSelectedPlace) {
            EmptyForecastBecauseNoSelectedPlace
        } else {
            EmptyForecastBecauseReason(
                reason = empty.reason?.toErrorResourceId()
            )
        }
    }

    private suspend fun handleCachedSuccess(cachedSuccess: ForecastResult.Cached) {
        handleSuccess(cachedSuccess.success)
        _outdatedForecast.value = OutdatedForecastUiModel(
            reason = cachedSuccess.reason?.toErrorResourceId()
        )
    }

    private fun handleNone() {
        // intentionally do nothing
    }
}