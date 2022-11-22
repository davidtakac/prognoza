package hr.dtakac.prognoza.ui.places

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import hr.dtakac.prognoza.presentation.OnEvent
import hr.dtakac.prognoza.presentation.places.PlacesViewModel

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel = hiltViewModel(),
    onPlaceSelected: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.state
    LaunchedEffect(true) {
        viewModel.getSaved()
    }
    OnEvent(state.placeSelected) {
        onPlaceSelected()
    }

    var query by remember { mutableStateOf("") }
    PlacesContent(
        state = state,
        onPlaceSelected = viewModel::select,
        onSettingsClick = onSettingsClick,
        query = query,
        onQuerySubmit = { viewModel.search(query) },
        onQueryChange = {
            query = it
            if (it.isBlank()) viewModel.getSaved()
        }
    )
}