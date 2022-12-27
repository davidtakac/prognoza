package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.settingsscreen.MultipleChoiceSettingUi
import hr.dtakac.prognoza.presentation.settingsscreen.SettingsScreenState
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.common.AppToolbar
import hr.dtakac.prognoza.ui.common.rememberAppToolbarState

@Composable
fun SettingsContent(
    state: SettingsScreenState,
    onBackClick: () -> Unit = {},
    onCreditAndLicensesClick: () -> Unit = {},
    onSourceClick: () -> Unit = {},
    onLicenseClick: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            val toolbarState = rememberAppToolbarState()
            AppToolbar(
                title = { Text(stringResource(id = R.string.settings)) },
                state = toolbarState,
                navigation = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
            SettingsList(
                state = state,
                isTitleVisible = { toolbarState.setTitleVisible(!it) },
                onCreditAndLicensesClick = onCreditAndLicensesClick,
                onSourceClick = onSourceClick,
                onLicenseClick = onLicenseClick
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppTheme {
    SettingsContent(state = fakeState)
}

private val fakeState: SettingsScreenState = SettingsScreenState(
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