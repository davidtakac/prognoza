package hr.dtakac.prognoza.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
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
        onBackClick = onBackClick
    )
}

private fun openLink(url: String, context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.met.no/en")))
    } catch (e: Exception) {
    }
}