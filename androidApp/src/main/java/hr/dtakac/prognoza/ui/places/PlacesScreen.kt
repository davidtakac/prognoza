package hr.dtakac.prognoza.ui.places

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.presentation.OnEvent
import hr.dtakac.prognoza.presentation.asString
import hr.dtakac.prognoza.presentation.places.PlacesViewModel
import hr.dtakac.prognoza.ui.common.AppDialog

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel(),
    onPlaceSelected: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.state
    var query by remember { mutableStateOf("") }
    var placeDeletionDialogIndex by remember { mutableStateOf<Int?>(null) }
    val focusManager = LocalFocusManager.current

    OnEvent(state.placeSelected) {
        query = ""
        focusManager.clearFocus()
        onPlaceSelected()
    }

    PlacesContent(
        state = state,
        onSettingsClick = onSettingsClick,
        onPlaceClick = { idx ->
            viewModel.selectPlace(idx)
        },
        onPlaceLongClick = { idx ->
            placeDeletionDialogIndex = idx
        },
        searchQuery = query,
        onSearchSubmit = {
            viewModel.getSearchResults(query)
        },
        onSearchQueryChange = { newQuery ->
            query = newQuery
            if (newQuery.isBlank()) {
                viewModel.getSaved()
            }
        }
    )

    placeDeletionDialogIndex?.let { idx ->
        val placeName = state.places[idx].name.asString()
        AppDialog(
            title = stringResource(id = R.string.delete_place_title, placeName),
            iconResId = R.drawable.ic_delete,
            onConfirm = {
                viewModel.deletePlace(idx)
                placeDeletionDialogIndex = null
            },
            confirmLabel = stringResource(id = R.string.delete),
            onDismiss = {
                placeDeletionDialogIndex = null
            }
        ) { Text(stringResource(id = R.string.template_delete_place_description)) }
    }
}