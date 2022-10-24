package hr.dtakac.prognoza.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onTemperatureUnitPick = viewModel::setTemperatureUnit,
        onWindUnitPick = viewModel::setWindUnit,
        onPrecipitationUnitPick = viewModel::setPrecipitationUnit,
        onPressureUnitPick = viewModel::setPressureUnit,
        onThemePick = viewModel::setTheme,
        onWeatherDataClick = { openMetNorwayWebsite(context) },
        onPlaceDataClick = { openOsmWebsite(context) }
    )
}

private fun openMetNorwayWebsite(context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.met.no/en")))
    } catch (e: Exception) {
    }
}

private fun openOsmWebsite(context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openstreetmap.org")))
    } catch (e: Exception) {
    }
}