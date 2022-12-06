package hr.dtakac.prognoza.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.OnEvent
import hr.dtakac.prognoza.presentation.settingsscreen.SettingsScreenViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onThemeChange: () -> Unit = {},
    onMoodModeChange: () -> Unit = {},
    onUnitChange: () -> Unit = {}
) {
    val state by viewModel.state
    LaunchedEffect(true) {
        viewModel.getState()
    }
    OnEvent(event = state.themeChangedEvent) {
        onThemeChange()
    }
    OnEvent(event = state.unitChangedEvent) {
        onUnitChange()
    }
    OnEvent(event = state.moodModeChangedEvent) {
        onMoodModeChange()
    }
    val context = LocalContext.current
    OnEvent(event = state.openLinkEvent) {
        openLink(link = it, context)
    }

    SettingsContent(
        state = state,
        onBackClick = onBackClick
    )
}

private fun openLink(link: String, context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    } catch (e: Exception) {
    }
}