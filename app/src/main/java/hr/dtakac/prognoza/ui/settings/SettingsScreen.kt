package hr.dtakac.prognoza.ui.settings

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onUnitChange: () -> Unit = {}
) {
    LaunchedEffect(true) {
        viewModel.getState()
    }
    val state by viewModel.state

    val themeChanged by viewModel.themeChanged
    LaunchedEffect(themeChanged) {
        if (themeChanged != null) {
            onThemeChange()
        }
    }

    val unitChanged by viewModel.unitChanged
    LaunchedEffect(unitChanged) {
        if (unitChanged != null) {
            onUnitChange()
        }
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