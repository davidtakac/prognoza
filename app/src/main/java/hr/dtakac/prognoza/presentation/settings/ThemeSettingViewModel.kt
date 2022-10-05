package hr.dtakac.prognoza.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.ui.ThemeSetting
import hr.dtakac.prognoza.ui.ThemeChanger
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingViewModel @Inject constructor(
    private val themeChanger: ThemeChanger
): ViewModel() {
    private val _currentTheme = mutableStateOf(ThemeSetting.FOLLOW_SYSTEM)
    val currentTheme: State<ThemeSetting> get() = _currentTheme

    fun getState() {
        viewModelScope.launch {
            _currentTheme.value = themeChanger.getCurrentTheme()
        }
    }
}