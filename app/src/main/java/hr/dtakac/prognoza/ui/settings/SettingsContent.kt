package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    onBackClick: () -> Unit = {},
    onTemperatureUnitPick: (Int) -> Unit = {},
    onWindUnitPick: (Int) -> Unit = {},
    onPrecipitationUnitPick: (Int) -> Unit = {},
    onPressureUnitPick: (Int) -> Unit = {},
    onThemePick: (Int) -> Unit = {},
    onWeatherDataClick: () -> Unit = {},
    onPlaceDataClick: () -> Unit = {}
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
                isTitleVisible = { toolbarTitleVisible = !it },
                onTemperatureUnitPick = onTemperatureUnitPick,
                onWindUnitPick = onWindUnitPick,
                onPrecipitationUnitPick = onPrecipitationUnitPick,
                onPressureUnitPick = onPressureUnitPick,
                onThemePick = onThemePick,
                onWeatherDataClick = onWeatherDataClick,
                onPlaceDataClick = onPlaceDataClick
            )
        }
    }
}

@Composable
private fun SettingsList(
    state: SettingsState,
    isTitleVisible: (Boolean) -> Unit,
    onTemperatureUnitPick: (Int) -> Unit,
    onWindUnitPick: (Int) -> Unit,
    onPrecipitationUnitPick: (Int) -> Unit,
    onPressureUnitPick: (Int) -> Unit,
    onThemePick: (Int) -> Unit,
    onWeatherDataClick: () -> Unit,
    onPlaceDataClick: () -> Unit,
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
        item(key = "settings-units-spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item(key = "units-spacer") {
            Header(
                text = stringResource(id = R.string.units),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )
        }
        state.temperatureUnitSetting?.let {
            item(key = "temperature-unit") {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = onTemperatureUnitPick
                )
            }
        }
        state.windUnitSetting?.let {
            item(key = "wind-unit") {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = onWindUnitPick
                )
            }
        }
        state.precipitationUnitSetting?.let {
            item(key = "precipitation-unit") {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = onPrecipitationUnitPick
                )
            }
        }
        state.pressureUnitSetting?.let {
            item(key = "pressure-unit") {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = onPressureUnitPick
                )
            }
        }
        item(key = "appearance-header") {
            Header(
                text = stringResource(id = R.string.appearance),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )
        }
        state.themeSetting?.let {
            item(key = "theme-setting") {
                MultipleChoiceSettingItem(
                    state = it,
                    onPick = onThemePick
                )
            }
        }
        item(key = "credit-header") {
            Header(
                text = stringResource(id = R.string.credit),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )
        }
        item(key = "weather-credit") {
            SettingItem(
                name = stringResource(id = R.string.weather_data),
                value = stringResource(id = R.string.met_norway_credit),
                onClick = onWeatherDataClick
            )
        }
        item(key = "place-credit") {
            SettingItem(
                name = stringResource(id = R.string.geolocation_data),
                value = stringResource(id = R.string.osm_nominatim_credit),
                onClick = onPlaceDataClick
            )
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
    temperatureUnitSetting = MultipleChoiceSetting(name = TextResource.fromText("Temperature unit"), value = TextResource.fromText("Celsius"), values = listOf()),
    windUnitSetting = MultipleChoiceSetting(name = TextResource.fromText("Wind unit"), value = TextResource.fromText("Kilometers per hour"), values = listOf()),
    precipitationUnitSetting = MultipleChoiceSetting(name = TextResource.fromText("Precipitation unit"), value = TextResource.fromText("Millimeters"), values = listOf()),
    pressureUnitSetting = MultipleChoiceSetting(name = TextResource.fromText("Pressure unit"), value = TextResource.fromText("Hectopascal"), values = listOf()),
    themeSetting = MultipleChoiceSetting(name = TextResource.fromText("Theme"), value = TextResource.fromText("Follow system setting"), values = listOf())
)