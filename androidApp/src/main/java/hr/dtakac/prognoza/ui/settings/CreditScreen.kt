package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.common.AppToolbar
import hr.dtakac.prognoza.ui.common.keyVisibilityPercent
import hr.dtakac.prognoza.ui.common.openLink
import hr.dtakac.prognoza.ui.common.rememberAppToolbarState
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map

@Composable
fun LicensesAndCreditScreen(
    onBackClick: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            val toolbarState = rememberAppToolbarState()
            AppToolbar(
                title = { Text(stringResource(id = R.string.credit_and_licenses_title)) },
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

            val context = LocalContext.current
            val listState = rememberLazyListState()
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged()
                    .dropWhile { it.totalItemsCount == 0 }
                    .map { it.keyVisibilityPercent("screen-title") != 0f }
                    .distinctUntilChanged()
                    .collect { toolbarState.setTitleVisible(!it) }
            }
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(top = 24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            ) {
                item(key = "screen-title") {
                    Text(
                        text = stringResource(id = R.string.credit_and_licenses_title),
                        style = PrognozaTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                item(key = "settings-spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item(key = "licenses-header") {
                    SettingsSectionHeader(
                        text = stringResource(id = R.string.licenses_title),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                item(key = "met-no") {
                    SettingItem(
                        name = stringResource(id = R.string.met_norway_credit),
                        value = stringResource(id = R.string.met_norway_credit_description),
                        onClick = { openLink("https://api.met.no/doc/License", context) }
                    )
                }
                item(key = "open-meteo") {
                    SettingItem(
                        name = stringResource(id = R.string.open_meteo_credit),
                        value = stringResource(id = R.string.open_meteo_credit_description),
                        onClick = { openLink("https://open-meteo.com/en/features#terms", context) }
                    )
                }
                item(key = "osm") {
                    SettingItem(
                        name = stringResource(id = R.string.osm_nominatim_credit),
                        value = stringResource(id = R.string.osm_nominatim_credit_description),
                        onClick = { openLink("https://www.openstreetmap.org/copyright", context) }
                    )
                }
                item(key = "weather-icons") {
                    SettingItem(
                        name = stringResource(id = R.string.weather_icons_title),
                        value = stringResource(id = R.string.weather_icons_description),
                        onClick = { openLink("https://api.met.no/doc/License", context) }
                    )
                }

                item(key = "credit-header") {
                    SettingsSectionHeader(
                        text = stringResource(id = R.string.credit),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                item(key = "design") {
                    SettingItem(
                        name = stringResource(id = R.string.design_credit),
                        value = stringResource(id = R.string.design_credit_description),
                        onClick = { openLink("https://dribbble.com/shots/6680361-Dribbble-Daily-UI-37-Weather-2", context) }
                    )
                }
                item(key = "app-icon") {
                    SettingItem(
                        name = stringResource(id = R.string.launcher_icon_credit),
                        value = stringResource(id = R.string.launcher_icon_description),
                        onClick = { openLink("https://www.instagram.com/art.ofil/", context) }
                    )
                }
            }
        }
    }
}