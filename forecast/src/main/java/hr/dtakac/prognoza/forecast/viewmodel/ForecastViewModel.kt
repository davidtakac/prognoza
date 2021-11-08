package hr.dtakac.prognoza.forecast.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import hr.dtakac.prognoza.core.model.database.ForecastMeta
import hr.dtakac.prognoza.core.model.database.Place
import hr.dtakac.prognoza.core.model.repository.CachedSuccess
import hr.dtakac.prognoza.core.model.repository.Empty
import hr.dtakac.prognoza.core.model.repository.ForecastResult
import hr.dtakac.prognoza.core.model.repository.Success
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.utils.hasExpired
import hr.dtakac.prognoza.core.utils.toErrorResourceId
import hr.dtakac.prognoza.forecast.model.EmptyForecastUiModel
import hr.dtakac.prognoza.forecast.model.ForecastUiModel
import hr.dtakac.prognoza.forecast.model.OutdatedForecastUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class ForecastViewModel<T : ForecastUiModel>(
    coroutineScope: CoroutineScope?,
    protected val preferencesRepository: PreferencesRepository,
    protected val placeRepository: PlaceRepository
) : hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel(coroutineScope) {
    private var currentMeta: ForecastMeta? = null

    protected abstract val _forecast: MutableState<T?>
    val forecast: State<T?> get() = _forecast

    protected lateinit var selectedPlace: Place

    private val _emptyScreen = mutableStateOf<EmptyForecastUiModel?>(null)
    val emptyScreen: State<EmptyForecastUiModel?> get() = _emptyScreen

    private val _outdatedForecastMessage = mutableStateOf<OutdatedForecastUiModel?>(null)
    val outdatedForecastMessage: State<OutdatedForecastUiModel?> get() = _outdatedForecastMessage

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun getForecast() {
        coroutineScope.launch {
            if (isReloadNeeded()) {
                _isLoading.value = true
                selectedPlace = placeRepository.get(preferencesRepository.getSelectedPlaceId())
                    ?: placeRepository.getDefaultPlace()
                when (val result = getNewForecast()) {
                    is Success -> handleSuccess(result)
                    is CachedSuccess -> handleCachedSuccess(result)
                    is Empty -> handleEmpty(result)
                }
                _isLoading.value = false
            }
        }
    }

    protected abstract suspend fun getNewForecast(): ForecastResult

    protected abstract suspend fun mapToForecastUiModel(success: Success): T

    private suspend fun handleSuccess(success: Success) {
        _forecast.value = mapToForecastUiModel(success)
        currentMeta = success.meta
        _emptyScreen.value = null
        _outdatedForecastMessage.value = null
    }

    private fun handleEmpty(empty: Empty) {
        _emptyScreen.value = EmptyForecastUiModel(empty.reason?.toErrorResourceId())
    }

    private suspend fun handleCachedSuccess(cachedResult: CachedSuccess) {
        handleSuccess(cachedResult.success)
        _outdatedForecastMessage.value = OutdatedForecastUiModel(
            reason = cachedResult.reason?.toErrorResourceId()
        )
    }

    private suspend fun isReloadNeeded(): Boolean {
        return currentMeta?.hasExpired() != false ||
                currentMeta?.placeId != preferencesRepository.getSelectedPlaceId() ||
                _forecast.value == null
    }
}