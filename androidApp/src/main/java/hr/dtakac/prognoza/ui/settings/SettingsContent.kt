package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.settings.MultipleChoiceSettingUi
import hr.dtakac.prognoza.presentation.settings.SettingsState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.common.AppToolbar
import hr.dtakac.prognoza.ui.common.rememberAppToolbarState

@Composable
fun SettingsContent(
    state: SettingsState,
    onBackClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides PrognozaTheme.colors.onSurface) {
        Column(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            val toolbarState = rememberAppToolbarState()
            AppToolbar(
                title = { Text(stringResource(id = R.string.settings)) },
                state = toolbarState,
                navigation = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                }
            )
            SettingsList(
                state = state,
                isTitleVisible = { toolbarState.setTitleVisible(!it) }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppTheme {
    SettingsContent(state = fakeState)
}

private val fakeState: SettingsState = SettingsState(
    isLoading = false,
    unitSettings = mutableListOf<MultipleChoiceSettingUi>().apply {
        repeat(5) {
            add(
                MultipleChoiceSettingUi(
                    name = TextResource.fromString("Setting $it"),
                    selectedIndex = 0,
                    values = listOf(),
                    onIndexSelected = {}
                )
            )
        }
    }
)