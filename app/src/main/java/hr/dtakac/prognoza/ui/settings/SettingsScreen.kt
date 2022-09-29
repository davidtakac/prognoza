package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.settings.SettingsViewModel
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun SettingsScreen(
    backgroundColor: Color,
    elevatedBackgroundColor: Color,
    onBackgroundColor: Color,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUnitChanged: () -> Unit = {}
) {
    LaunchedEffect(viewModel) { viewModel.getState() }
    val state by remember { viewModel.state }

    CompositionLocalProvider(LocalContentColor provides onBackgroundColor) {
        Column {
            Toolbar(
                onBackClick = onBackClick,
                modifier = Modifier.background(elevatedBackgroundColor)
            )
            LazyColumn(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
            ) {
                item {
                    Header(
                        text = stringResource(id = R.string.units),
                        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                state.temperatureUnitSetting?.let {
                    item {
                        Setting(
                            name = it.name.asString(),
                            value = it.value.asString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Toolbar(
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
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.settings),
                style = PrognozaTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun Header(text: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = text, style = PrognozaTheme.typography.titleSmall)
        Divider(color = LocalContentColor.current, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun Setting(
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
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(text = name, style = PrognozaTheme.typography.titleMedium)
        Text(
            text = value,
            style = PrognozaTheme.typography.subtitleMedium,
        )
    }
}