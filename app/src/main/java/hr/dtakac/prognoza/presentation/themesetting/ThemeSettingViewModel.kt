package hr.dtakac.prognoza.presentation.themesetting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.dtakac.prognoza.themesettings.ThemeSetting
import hr.dtakac.prognoza.themesettings.usecase.GetThemeSetting
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingViewModel @Inject constructor(
    private val getThemeSetting: GetThemeSetting
): ViewModel() {
    private val _themeSetting = mutableStateOf(ThemeSetting.FOLLOW_SYSTEM)
    val themeSetting: State<ThemeSetting> get() = _themeSetting

    fun getState() {
        viewModelScope.launch {
            _themeSetting.value = getThemeSetting()
        }
    }
}