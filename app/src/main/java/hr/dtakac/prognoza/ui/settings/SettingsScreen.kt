package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.settings.SettingsViewModel
import hr.dtakac.prognoza.presentation.settings.MultipleChoiceSetting
import hr.dtakac.prognoza.ui.forecast.SlideUpAppearText
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onThemeSettingChange: () -> Unit = {}
) {
    LaunchedEffect(viewModel) { viewModel.getState() }
    val state by remember { viewModel.state }

    CompositionLocalProvider(LocalContentColor provides PrognozaTheme.onBackgroundColor) {
        Column {
            var toolbarSettingsVisible by remember { mutableStateOf(false) }
            Toolbar(
                settingsVisible = toolbarSettingsVisible,
                onBackClick = onBackClick,
                modifier = Modifier.background(PrognozaTheme.elevatedBackgroundColor)
            )

            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .background(PrognozaTheme.backgroundColor)
                    .fillMaxSize(),
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
                        UnitSetting(
                            unitSetting = it,
                            onConfirm = viewModel::setTemperatureUnit
                        )
                    }
                }
                state.windUnitSetting?.let {
                    item {
                        UnitSetting(
                            unitSetting = it,
                            onConfirm = viewModel::setWindUnit
                        )
                    }
                }
                state.precipitationUnitSetting?.let {
                    item {
                        UnitSetting(
                            unitSetting = it,
                            onConfirm = viewModel::setPrecipitationUnit
                        )
                    }
                }
                state.pressureUnitSetting?.let {
                    item {
                        UnitSetting(
                            unitSetting = it,
                            onConfirm = viewModel::setPressureUnit
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
                        UnitSetting(
                            unitSetting = it,
                            onConfirm = viewModel::setTheme
                        )
                        LaunchedEffect(it.values.indexOf(it.value)) {
                            onThemeSettingChange()
                        }
                    }
                }
            }
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged()
                    .map { it.keyVisibilityPercent("settings") }
                    .distinctUntilChanged()
                    .collect { settingsVis ->
                        toolbarSettingsVisible = settingsVis == 0f
                    }
            }
        }
    }
}

@Composable
private fun Toolbar(
    settingsVisible: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(90.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            SlideUpAppearText(
                text = stringResource(id = R.string.settings),
                visible = settingsVisible,
                style = PrognozaTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun Header(text: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = PrognozaTheme.typography.titleSmall,
            //color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
        )
        //Divider(color = LocalContentColor.current, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun UnitSetting(
    unitSetting: MultipleChoiceSetting,
    onConfirm: (Int) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    SettingItem(
        name = unitSetting.name.asString(),
        value = unitSetting.value.asString(),
        onClick = { openDialog = true }
    )
    if (openDialog) {
        SettingDialog(
            title = unitSetting.name.asString(),
            selectedOption = unitSetting.value.asString(),
            options = unitSetting.values.map { it.asString() },
            onConfirm = onConfirm,
            onDismiss = { openDialog = false }
        )
    }
}

@Composable
private fun SettingItem(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = name, style = PrognozaTheme.typography.subtitleMedium)
        Text(
            text = value,
            style = PrognozaTheme.typography.subtitleMedium,
            color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
        )
    }
}

@Composable
private fun SettingDialog(
    title: String,
    selectedOption: String,
    options: List<String>,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(options.indexOf(selectedOption)) }
    AlertDialog(
        containerColor = PrognozaTheme.elevatedBackgroundColor,
        titleContentColor = PrognozaTheme.onBackgroundColor,
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = PrognozaTheme.typography.titleMedium) },
        text = {
            CompositionLocalProvider(LocalContentColor provides PrognozaTheme.onBackgroundColor) {
                LazyColumn {
                    itemsIndexed(options) { idx, option ->
                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = { selectedIndex = idx },
                                    indication = rememberRipple(bounded = true),
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = idx == selectedIndex,
                                onClick = null,
                                interactionSource = remember { MutableInteractionSource() },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                                    unselectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                                    disabledSelectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.disabled),
                                    disabledUnselectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.disabled)
                                )
                            )
                            Text(
                                text = option,
                                style = PrognozaTheme.typography.subtitleMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedIndex)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    style = PrognozaTheme.typography.titleSmall,
                    color = PrognozaTheme.onBackgroundColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = PrognozaTheme.onBackgroundColor,
                    style = PrognozaTheme.typography.titleSmall
                )
            }
        }
    )
}