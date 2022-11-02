package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.settings.MultipleChoiceSetting
import hr.dtakac.prognoza.presentation.settings.SettingsState
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.common.PrognozaToolbar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun SettingsContent(
    state: SettingsState,
    onBackClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalContentColor provides PrognozaTheme.colors.onSurface) {
        Column(modifier = Modifier.background(PrognozaTheme.colors.surface1)) {
            var toolbarTitleVisible by remember { mutableStateOf(false) }
            PrognozaToolbar(
                title = { Text(stringResource(id = R.string.settings)) },
                titleVisible = toolbarTitleVisible,
                navigationIcon = {
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
                isTitleVisible = { toolbarTitleVisible = !it }
            )
        }
    }
}

@Composable
private fun SettingsList(
    state: SettingsState,
    isTitleVisible: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.asPaddingValues()),
        contentPadding = PaddingValues(vertical = 24.dp),
        state = listState
    ) {
        item(key = "settings") {
            Text(
                text = stringResource(id = R.string.settings),
                style = PrognozaTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item(key = "settings-spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }

        state.unitSettings.takeIf { it.isNotEmpty() }?.let { settings ->
            item(key = "units-spacer") {
                Header(
                    text = stringResource(id = R.string.units),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            }
            items(settings) {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = it.onValuePick
                )
            }
        }

        state.appearanceSettings.takeIf { it.isNotEmpty() }?.let { settings ->
            item(key = "appearance-header") {
                Header(
                    text = stringResource(id = R.string.appearance),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            }
            items(settings) {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = it.onValuePick
                )
            }
        }

        state.creditSettings.takeIf { it.isNotEmpty() }?.let { settings ->
            item(key = "credit-header") {
                Header(
                    text = stringResource(id = R.string.credit),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            }
            items(settings) {
                SettingItem(
                    name = it.name.asString(),
                    value = it.value.asString(),
                    onClick = it.onClick
                )
            }
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .distinctUntilChanged()
            .map { it.keyVisibilityPercent("settings") != 0f }
            .distinctUntilChanged()
            .collect(isTitleVisible)
    }
}

@Composable
private fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = PrognozaTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun Preview() = PrognozaTheme {
    SettingsContent(state = fakeState)
}

private val fakeState: SettingsState = SettingsState(
    isLoading = false,
    unitSettings = mutableListOf<MultipleChoiceSetting>().apply {
        repeat(5) {
            add(
                MultipleChoiceSetting(
                    name = TextResource.fromText("Setting $it"),
                    value = TextResource.fromText("Value $it"),
                    values = listOf(),
                    onValuePick = {}
                )
            )
        }
    }
)