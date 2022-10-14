package hr.dtakac.prognoza.ui.settings

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.OnEvent
import hr.dtakac.prognoza.presentation.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onUnitChange: () -> Unit = {}
) {
    val state by viewModel.state
    LaunchedEffect(true) {
        viewModel.getState()
    }
    OnEvent(event = state.themeChanged) {
        onThemeChange()
    }
    OnEvent(event = state.unitChanged) {
        onUnitChange()
    }

    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onTemperatureUnitPick = viewModel::setTemperatureUnit,
        onWindUnitPick = viewModel::setWindUnit,
        onPrecipitationUnitPick = viewModel::setPrecipitationUnit,
        onPressureUnitPick = viewModel::setPressureUnit,
        onThemePick = viewModel::setTheme
    )
}