package hr.dtakac.prognoza.presentation.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeSettingViewModel @Inject constructor(
    private val sharedPrefsThemeSettingRepository: SharedPrefsThemeSettingRepository
): ViewModel() {
    private val _currentTheme = mutableStateOf(sharedPrefsThemeSettingRepository.getTheme())
    val currentTheme: State<ThemeSetting> get() = _currentTheme

    fun getState() {
        _currentTheme.value = sharedPrefsThemeSettingRepository.getTheme()
    }
}