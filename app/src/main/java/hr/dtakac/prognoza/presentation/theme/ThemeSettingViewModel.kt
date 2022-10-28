package hr.dtakac.prognoza.presentation.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingViewModel @Inject constructor(
    private val themeSettingRepository: ThemeSettingRepository
): ViewModel() {
    private val _currentTheme = mutableStateOf(ThemeSetting.FOLLOW_SYSTEM)
    val currentTheme: State<ThemeSetting> get() = _currentTheme

    fun getState() {
        viewModelScope.launch {
            _currentTheme.value = themeSettingRepository.getCurrentTheme()
        }
    }
}