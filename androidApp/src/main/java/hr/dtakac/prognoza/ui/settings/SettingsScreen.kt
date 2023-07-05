package hr.dtakac.prognoza.ui.settings

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.settings.SettingsScreenViewModel
import hr.dtakac.prognoza.ui.common.OnEvent
import hr.dtakac.prognoza.ui.common.openLink

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCreditAndLicensesClick: () -> Unit = {},
    updateTheme: () -> Unit = {},
    updateForecast: () -> Unit = {}
) {
    val state by viewModel.state
    OnEvent(event = state.updateThemeEvent) {
        updateTheme()
    }
    OnEvent(event = state.updateForecastEvent) {
        updateForecast()
    }

    val context = LocalContext.current
    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onCreditAndLicensesClick = onCreditAndLicensesClick,
        onSourceClick = { openLink("https://github.com/davidtakac/prognoza", context) },
        onLicenseClick = { openLink("https://github.com/davidtakac/prognoza/blob/dev/LICENSE", context) }
    )
}