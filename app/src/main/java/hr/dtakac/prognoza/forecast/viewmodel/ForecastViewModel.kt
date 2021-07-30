package hr.dtakac.prognoza.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.dtakac.prognoza.base.CoroutineScopeViewModel
import hr.dtakac.prognoza.database.entity.shortenedName
import hr.dtakac.prognoza.repository.place.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForecastViewModel(
    coroutineScope: CoroutineScope?,
    private val placeRepository: PlaceRepository
) : CoroutineScopeViewModel(coroutineScope) {
    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    fun getPlaceName() {
        coroutineScope.launch {
            _placeName.value = placeRepository.getSelectedPlace().shortenedName
        }
    }
}