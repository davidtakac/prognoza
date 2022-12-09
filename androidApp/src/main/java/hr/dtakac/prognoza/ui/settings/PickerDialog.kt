package hr.dtakac.prognoza.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.ui.theme.AppTheme
import hr.dtakac.prognoza.ui.theme.PrognozaTheme

@Composable
fun PickerDialog(
    title: String,
    selectedIndex: Int,
    options: List<String>,
    onSelectedIndexChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = PrognozaTheme.colors.onSurface,
    backgroundColor: Color = PrognozaTheme.colors.surface3
) {
    AlertDialog(
        modifier = modifier,
        containerColor = backgroundColor,
        titleContentColor = contentColor,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = PrognozaTheme.typography.titleMedium
            )
        },
        text = {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                OptionsList(
                    options = options,
                    onIndexSelect = onSelectedIndexChange,
                    selectedIndex = selectedIndex
                )
            }
        },
        confirmButton = {
            Button(
                label = stringResource(id = R.string.confirm),
                textColor = contentColor,
                onClick = onConfirm
            )
        },
        dismissButton = {
            Button(
                label = stringResource(id = R.string.cancel),
                textColor = contentColor,
                onClick = onDismiss
            )
        }
    )
}

@Composable
private fun Button(
    label: String,
    onClick: () -> Unit,
    textColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = label,
            color = textColor,
            style = PrognozaTheme.typography.titleSmall
        )
    }
}

@Composable
private fun OptionsList(
    options: List<String>,
    selectedIndex: Int,
    onIndexSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(options) { idx, option ->
            Option(
                label = option,
                isSelected = idx == selectedIndex,
                onClick = { onIndexSelect(idx) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun Option(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                unselectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.medium),
                disabledSelectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.disabled),
                disabledUnselectedColor = LocalContentColor.current.copy(alpha = PrognozaTheme.alpha.disabled)
            )
        )
        Text(
            text = label,
            style = PrognozaTheme.typography.subtitleMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
private fun PickerDialogPreview() = AppTheme {
    PickerDialog(
        title = "Temperature",
        selectedIndex = 0,
        options = listOf("Degree Celsius", "Degree Fahrenheit"),
        onSelectedIndexChange = {},
        onConfirm = {},
        onDismiss = {}
    )
}