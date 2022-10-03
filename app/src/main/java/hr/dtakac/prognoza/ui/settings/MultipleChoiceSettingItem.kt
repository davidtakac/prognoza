package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.TextResource
import hr.dtakac.prognoza.presentation.settings.MultipleChoiceSetting
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun MultipleChoiceSettingItem(
    state: MultipleChoiceSetting,
    onPick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(false) }
    Content(
        name = state.name.asString(),
        value = state.value.asString(),
        onClick = { openDialog = true },
        modifier = modifier
    )
    if (openDialog) {
        PickerDialog(
            title = state.name.asString(),
            selectedOption = state.value.asString(),
            options = state.values.map { it.asString() },
            onConfirm = onPick,
            onDismiss = { openDialog = false }
        )
    }
}

@Composable
private fun Content(
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
            style = PrognozaTheme.typography.body,
            color = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium)
        )
    }
}

@Composable
private fun PickerDialog(
    title: String,
    selectedOption: String,
    options: List<String>,
    onConfirm: (Int) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var selectedIndex by remember { mutableStateOf(options.indexOf(selectedOption)) }
    AlertDialog(
        containerColor = PrognozaTheme.colors.surface3,
        titleContentColor = PrognozaTheme.colors.onSurface,
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = PrognozaTheme.typography.titleMedium) },
        text = {
            CompositionLocalProvider(LocalContentColor provides PrognozaTheme.colors.onSurface) {
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
                    color = PrognozaTheme.colors.onSurface
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = PrognozaTheme.colors.onSurface,
                    style = PrognozaTheme.typography.titleSmall
                )
            }
        }
    )
}

@Preview
@Composable
private fun ContentPreview() = PrognozaTheme {
    Content(name = fakeState.name.asString(), value = fakeState.value.asString())
}

@Preview
@Composable
private fun PickerDialogPreview() = PrognozaTheme {
    PickerDialog(
        title = fakeState.name.asString(),
        selectedOption = fakeState.value.asString(),
        options = fakeState.values.map { it.asString() }
    )
}

private val fakeState: MultipleChoiceSetting = MultipleChoiceSetting(
    name = TextResource.fromText("Temperature unit"),
    value = TextResource.fromText("Celsius"),
    values = listOf(
        TextResource.fromText("Celsius"),
        TextResource.fromText("Fahrenheit")
    )
)