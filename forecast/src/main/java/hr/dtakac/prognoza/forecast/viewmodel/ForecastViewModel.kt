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
import hr.dtakac.prognoza.core.repository.meta.MetaRepository
import hr.dtakac.prognoza.core.repository.place.PlaceRepository
import hr.dtakac.prognoza.core.repository.preferences.PreferencesRepository
import hr.dtakac.prognoza.core.utils.hasExpired
import hr.dtakac.prognoza.core.utils.toErrorResourceId
import hr.dtakac.prognoza.core.viewmodel.CoroutineScopeViewModel
import hr.dtakac.prognoza.forecast.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class ForecastViewModel<T : ForecastUiModel>(
    coroutineScope: CoroutineScope?,
    private val preferencesRepository: PreferencesRepository,
    private val placeRepository: PlaceRepository,
    protected val metaRepository: MetaRepository
) : CoroutineScopeViewModel(coroutineScope) {

    private var currentMeta: ForecastMeta? = null
    private var currentPlace: Place? = null

    protected abstract val _forecast: MutableState<T?>
    val forecast: State<T?> get() = _forecast

    private val _emptyForecast = mutableStateOf<EmptyForecastUiModel?>(null)
    val emptyForecast: State<EmptyForecastUiModel?> get() = _emptyForecast

    private val _outdatedForecast = mutableStateOf<OutdatedForecastUiModel?>(null)
    val outdatedForecast: State<OutdatedForecastUiModel?> get() = _outdatedForecast

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun getForecast() {
        coroutineScope.launch {
            val selectedPlaceId = preferencesRepository.getSelectedPlaceId()
            if (selectedPlaceId == null) {
                handleNoSelectedPlace()
            } else {
                val isReloadNeeded = isReloadNeeded(
                    meta = currentMeta,
                    selectedPlaceId = selectedPlaceId,
                    forecast = _forecast.value
                )
                if (isReloadNeeded) {
                    _isLoading.value = true
                    val selectedPlace = placeRepository.get(placeId = selectedPlaceId)
                    currentMeta = selectedPlaceId.let { metaRepository.get(placeId = it) }
                    when (val result = getNewForecast(place = selectedPlace, meta = currentMeta)) {
                        is Success -> handleSuccess(result, selectedPlace)
                        is CachedSuccess -> handleCachedSuccess(result, selectedPlace)
                        is Empty -> handleEmpty(result)
                    }
                    currentPlace = selectedPlace
                    currentMeta = metaRepository.get(placeId = selectedPlace.id)
                    _isLoading.value = false
                }
            }
        }
    }

    protected abstract suspend fun getNewForecast(
        place: Place,
        meta: ForecastMeta?
    ): ForecastResult

    protected abstract suspend fun mapToForecastUiModel(
        success: Success,
        place: Place
    ): T

    private suspend fun handleSuccess(
        success: Success,
        place: Place
    ) {
        _forecast.value = mapToForecastUiModel(success, place)
        _emptyForecast.value = null
        _outdatedForecast.value = null
    }

    private fun handleEmpty(empty: Empty) {
        _emptyForecast.value = EmptyForecastBecauseReason(
            reason = empty.reason?.toErrorResourceId()
        )
    }

    private suspend fun handleCachedSuccess(
        cachedResult: CachedSuccess,
        place: Place
    ) {
        handleSuccess(cachedResult.success, place)
        _outdatedForecast.value = OutdatedForecastUiModel(
            reason = cachedResult.reason?.toErrorResourceId()
        )
    }

    private fun isReloadNeeded(
        meta: ForecastMeta?,
        selectedPlaceId: String?,
        forecast: ForecastUiModel?
    ): Boolean {
        return meta.hasExpired() ||
                meta?.placeId != selectedPlaceId ||
                forecast == null
    }

    private fun handleNoSelectedPlace() {
        _emptyForecast.value = EmptyForecastBecauseNoSelectedPlace
    }
}