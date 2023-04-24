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
                title = { Text(stringResource(id = R.string.settings_title_third_party_licenses)) },
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
                        text = stringResource(id = R.string.settings_title_third_party_licenses),
                        style = PrognozaTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                item(key = "settings-spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item(key = "licenses-header") {
                    SettingsSectionHeader(
                        text = stringResource(id = R.string.settings_heading_third_party_licenses),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                item(key = "open-meteo") {
                    SettingItem(
                        name = stringResource(id = R.string.settings_title_open_meteo),
                        value = stringResource(id = R.string.settings_msg_open_meteo),
                        onClick = { openLink("https://open-meteo.com/en/features#terms", context) }
                    )
                }
                item(key = "weather-icons") {
                    SettingItem(
                        name = stringResource(id = R.string.settings_title_weather_icons),
                        value = stringResource(id = R.string.settings_msg_weather_icons),
                        onClick = { openLink("https://api.met.no/doc/License", context) }
                    )
                }

                item(key = "credit-header") {
                    SettingsSectionHeader(
                        text = stringResource(id = R.string.settings_heading_credit),
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    )
                }
                item(key = "app-icon") {
                    SettingItem(
                        name = stringResource(id = R.string.settings_title_app_icon),
                        value = stringResource(id = R.string.settings_msg_app_icon),
                        onClick = { openLink("https://www.instagram.com/art.ofil/", context) }
                    )
                }
            }
        }
    }
}