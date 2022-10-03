package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.settings.SettingsViewModel
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import hr.dtakac.prognoza.ui.toolbar.PrognozaToolbar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onThemeSettingChange: () -> Unit = {},
    onUnitChange: () -> Unit = {}
) {
    LaunchedEffect(viewModel) {
        viewModel.getState()
    }
    val state by remember { viewModel.state }

    CompositionLocalProvider(LocalContentColor provides PrognozaTheme.colors.onSurface) {
        Column {
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

            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .background(PrognozaTheme.colors.surface1)
                    .fillMaxSize()
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                state = listState
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item(key = "settings") {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = PrognozaTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Header(
                        text = stringResource(id = R.string.units),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                state.temperatureUnitSetting?.let {
                    item {
                        MultipleChoiceSettingItem(
                            state = it,
                            onPick = { idx ->
                                viewModel.setTemperatureUnit(idx)
                                onUnitChange()
                            }
                        )
                    }
                }
                state.windUnitSetting?.let {
                    item {
                        MultipleChoiceSettingItem(
                            state = it,
                            onPick = { idx ->
                                viewModel.setWindUnit(idx)
                                onUnitChange()
                            }
                        )
                    }
                }
                state.precipitationUnitSetting?.let {
                    item {
                        MultipleChoiceSettingItem(
                            state = it,
                            onPick = { idx ->
                                viewModel.setPrecipitationUnit(idx)
                                onUnitChange()
                            }
                        )
                    }
                }
                state.pressureUnitSetting?.let {
                    item {
                        MultipleChoiceSettingItem(
                            state = it,
                            onPick = { idx ->
                                viewModel.setPressureUnit(idx)
                                onUnitChange()
                            }
                        )
                    }
                }
                item {
                    Header(
                        text = stringResource(id = R.string.appearance),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                state.themeSetting?.let {
                    item {
                        MultipleChoiceSettingItem(
                            state = it,
                            onPick = { idx ->
                                viewModel.setTheme(idx)
                                onThemeSettingChange()
                            }
                        )
                    }
                }
            }
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged()
                    .map { it.keyVisibilityPercent("settings") }
                    .distinctUntilChanged()
                    .collect { settingsVis ->
                        toolbarTitleVisible = settingsVis == 0f
                    }
            }
        }
    }
}

@Composable
private fun Header(text: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = PrognozaTheme.typography.titleSmall,
        )
    }
}