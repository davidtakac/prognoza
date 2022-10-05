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
    LaunchedEffect(true) { viewModel.getState() }
    val state by viewModel.state

    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onTemperatureUnitPick = { idx ->
            viewModel.setTemperatureUnit(idx)
            onUnitChange()
        },
        onWindUnitPick = { idx ->
            viewModel.setWindUnit(idx)
            onUnitChange()
        },
        onPrecipitationUnitPick = { idx ->
            viewModel.setPrecipitationUnit(idx)
            onUnitChange()
        },
        onPressureUnitPick = { idx ->
            viewModel.setPressureUnit(idx)
            onUnitChange()
        },
        onThemePick = { idx ->
            viewModel.setTheme(idx)
            onThemeChange()
        }
    )
}