package hr.dtakac.prognoza.androidsettings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidSettingsViewModel @Inject constructor(
  private val androidSettingsRepository: AndroidSettingsRepository
) : ViewModel() {
  private val _state = mutableStateOf(createState())
  val state: State<AndroidSettingsState> get() = _state

  fun getState() {
    _state.value = createState()
  }

  private fun createState() = AndroidSettingsState(
    uiMode = androidSettingsRepository.getUiMode(),
    moodMode = androidSettingsRepository.getMoodMode()
  )
}