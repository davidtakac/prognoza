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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.dtakac.prognoza.ui.common.AppDialog
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
    modifier: Modifier = Modifier
) {
    AppDialog(
        title = title,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        OptionsList(
            options = options,
            onIndexSelect = onSelectedIndexChange,
            selectedIndex = selectedIndex
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
            onClick = null
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