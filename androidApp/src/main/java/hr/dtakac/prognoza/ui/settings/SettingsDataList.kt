package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.settings.SettingsState
import hr.dtakac.prognoza.ui.forecast.keyVisibilityPercent
import hr.dtakac.prognoza.ui.theme.PrognozaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map

@Composable
fun SettingsList(
    state: SettingsState,
    isTitleVisible: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    ItemVisibilityChanges(
        listState = listState,
        isTitleVisible = isTitleVisible
    )

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
                SettingsSectionHeader(
                    text = stringResource(id = R.string.units),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            }
            items(settings) {
                PickerDialogSettingItem(
                    state = it,
                    onPick = it.onIndexSelected
                )
            }
        }

        state.appearanceSettings.takeIf { it.isNotEmpty() }?.let { settings ->
            item(key = "appearance-header") {
                SettingsSectionHeader(
                    text = stringResource(id = R.string.appearance),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            }
            items(settings) {
                PickerDialogSettingItem(
                    state = it,
                    onPick = it.onIndexSelected
                )
            }
        }

        state.creditSettings.takeIf { it.isNotEmpty() }?.let { settings ->
            item(key = "credit-header") {
                SettingsSectionHeader(
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

}

@Composable
private fun ItemVisibilityChanges(
    listState: LazyListState,
    isTitleVisible: (Boolean) -> Unit
) {
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .distinctUntilChanged()
            .dropWhile { it.totalItemsCount == 0 }
            .map { it.keyVisibilityPercent("settings") != 0f }
            .distinctUntilChanged()
            .collect(isTitleVisible)
    }
}