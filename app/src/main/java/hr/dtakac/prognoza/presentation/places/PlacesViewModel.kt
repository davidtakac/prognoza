package hr.dtakac.prognoza.presentation.places

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.domain.usecase.GetSavedPlaces
import hr.dtakac.prognoza.domain.usecase.SearchPlaces
import hr.dtakac.prognoza.domain.usecase.SelectPlace
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlaces: SearchPlaces,
    private val getSavedPlaces: GetSavedPlaces,
    private val selectPlace: SelectPlace
) : ViewModel() {

}