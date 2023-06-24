package hr.dtakac.prognoza.ui.settings

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import hr.dtakac.prognoza.presentation.settings.MultipleChoiceSettingUi

@Composable
fun PickerDialogSettingItem(
    state: MultipleChoiceSettingUi,
    onPick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val name = state.name.asString()
    var openDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(state.selectedIndex) }
    SettingItem(
        name = name,
        value = state.values[state.selectedIndex].asString(),
        onClick = { openDialog = true },
        modifier = modifier
    )
    if (openDialog) {
        PickerDialog(
            title = name,
            selectedIndex = selectedIndex,
            options = state.values.map { it.asString() },
            onSelectedIndexChange = {
                selectedIndex = it
            },
            onConfirm = {
                onPick(selectedIndex)
                openDialog = false
            },
            onDismiss = {
                openDialog = false
            }
        )
    }
}