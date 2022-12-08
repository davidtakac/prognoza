package hr.dtakac.prognoza.androidsettings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidSettingsViewModel @Inject constructor(
    private val androidSettingsRepository: AndroidSettingsRepository
): ViewModel() {
    private val _state = mutableStateOf(createState())
    val state: State<AndroidSettingsState> get() = _state

    // Uncomment to test color switching
    /*init {
        viewModelScope.launch {
            while(true) {
                delay(2500L)
                _state.value = _state.value.copy(
                    uiMode = listOf(UiMode.LIGHT, UiMode.DARK).random()
                )
            }
        }
    }*/

    fun getState() {
        _state.value = createState()
    }

    private fun createState() = AndroidSettingsState(
        uiMode = androidSettingsRepository.getUiMode(),
        moodMode = androidSettingsRepository.getMoodMode()
    )
}